package nsu_laboratory.app;

import nsu_laboratory.utils.MessageType;
import nsu_laboratory.utils.TorrentException;
import nsu_laboratory.utils.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import static nsu_laboratory.utils.Utility.*;

public class PeerServer implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger("PEER_SERVER");
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private int active = 0;

    public PeerServer() {
    }

    @Override
    public void run() {
        LOGGER.info("PeerServer started");
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress("localhost", PORT));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            LOGGER.info("Server is listening on port {}", PORT);

            while (true) {
                LOGGER.debug("Waiting for select . . .");
                LOGGER.debug("My current bitset : {}", Utility.bitfieldToByteArray(BITFIELD));
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        handleAcceptable(key);
                    } else if (key.isReadable()) {
                        handleReadable(key);
                    }
                }
                if (active == 0 && BITFIELD.cardinality() == SIZE) {
                    shutdownServer();
                    break;
                }
            }
        } catch (IOException e) {
            LOGGER.error("Get IOException while working: {}", e.getMessage());
            throw new TorrentException("Error while listening on port " + PORT, e);
        }
    }

    private void handleAcceptable(SelectionKey key) throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        active += 1;
        LOGGER.info("Accepted connection from {}", socketChannel.getRemoteAddress().toString());
    }

    private void handleReadable(SelectionKey key) throws IOException {
        LOGGER.debug("Message received on server:{}", PORT);
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER);
        if (socketChannel.read(buffer) < 0) {
            LOGGER.info("Connection closed, readable ");
            socketChannel.close();
            active -= 1;
            key.cancel();
            return;
        }
        buffer.flip();
        buffer.mark();
        byte pstrlen = buffer.get();
        if (pstrlen == 19) {
            LOGGER.debug("Handshake message received");
            handleHandshake(socketChannel, buffer);
        } else {
            buffer.reset();
            int len = buffer.getInt();
            byte id = buffer.get();
            LOGGER.debug("Current legnth: {}", len);
            switch (id) {
                case 3 -> handleNotInterested(key);
                case 5 -> handleBitfield(socketChannel, buffer, len);
                case 6 -> handleRequest(socketChannel, buffer);
            }

        }
    }

    private void handleNotInterested(SelectionKey key) throws IOException {
        LOGGER.info("Received 'not interested' from peer, disconnecting...");
        SocketChannel socketChannel = (SocketChannel) key.channel();
        active -= 1;
        key.cancel();
        socketChannel.close();
        if (active == 0 && BITFIELD.cardinality() == SIZE) {
            shutdownServer();
        }
    }

    private void shutdownServer() {
        try {
            selector.close();
            serverSocketChannel.close();
            LOGGER.info("Server has been shut down.");
        } catch (IOException e) {
            LOGGER.error("Error shutting down server", e);
        }
    }

    private void handleRequest(SocketChannel socketChannel, ByteBuffer buffer) throws IOException {
        int index = buffer.getInt();
        int offset = buffer.getInt();
        int length = buffer.getInt();
        LOGGER.debug("Handling request for index : {} | length: {} ", index, length);
        byte[] bytes = new byte[length];
        long start = (long) index * length + offset;
        FILE.seek(start);
        int bytesRead = 0;
        while (bytesRead != length) {
            bytesRead += FILE.read(bytes);
        }
        if (bytesRead == 0) {
            LOGGER.error("Error reading file");
            throw new TorrentException("Error reading file");
        }
        int len = bytes.length + 13;
        ByteBuffer piece = ByteBuffer.allocate(len);
        piece.putInt(len);
        piece.put((byte) 7);
        piece.putInt(index);
        piece.putInt(offset);
        piece.put(bytes);
        piece.flip();
        int written = 0;
        while (written < len) {
            written += socketChannel.write(piece);
        }
        socketChannel.keyFor(selector).interestOps(SelectionKey.OP_READ);
        LOGGER.info("Server:{}, Send piece {}", PORT, index);
    }

    private void handleBitfield(SocketChannel socketChannel, ByteBuffer buffer, int length) throws IOException {
        byte[] bitfield = new byte[length - 1];
        buffer.get(bitfield);
        LOGGER.info("Bitfield received from client: {}", Arrays.toString(bitfield));
        ByteBuffer responseBuffer = ByteBuffer.allocate(SIZE + 5);
        responseBuffer.putInt(1 + SIZE);
        responseBuffer.put(MessageType.BITFIELD.getId());
        responseBuffer.put(bitfieldToByteArray(BITFIELD));
        responseBuffer.flip();
        socketChannel.write(responseBuffer);
        socketChannel.keyFor(selector).interestOps(SelectionKey.OP_READ);
        LOGGER.info("Bitfield send back to client");
    }

    private void handleHandshake(SocketChannel socketChannel, ByteBuffer buffer) throws
            IOException, TorrentException {
        byte[] pstr = new byte[19];
        buffer.get(pstr, 0, 19);
        if (!("BitTorrent protocol").equals(new String(pstr))) {
            LOGGER.error("Wrong protocol received from peer");
            throw new TorrentException("Handshake failed - protocol");
        }
        buffer.position(28);
        byte[] infoHash = new byte[20];
        buffer.get(infoHash);
        if (!Arrays.equals(infoHash, INFO_HASH)) {
            LOGGER.error("Wrong info hash received from peer");
            throw new TorrentException("Handshake failed - info hash");
        }
        byte[] peerId = new byte[20];
        buffer.get(peerId);
        LOGGER.info("Send handshake back to client");
        sendHandshake(socketChannel);
    }

    private void sendHandshake(SocketChannel socketChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(HANDSHAKE_SIZE);
        buffer.put((byte) 19);
        buffer.put("BitTorrent protocol".getBytes());
        buffer.put(new byte[8]);
        buffer.put(INFO_HASH);
        buffer.put(PEER_ID);
        buffer.flip();
        socketChannel.write(buffer);
        socketChannel.keyFor(selector).interestOps(SelectionKey.OP_READ);
        LOGGER.info("Handshake message sent back to peer");
    }
}
