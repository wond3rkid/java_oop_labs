package nsu_laboratory.app;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import nsu_laboratory.utils.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;

import static nsu_laboratory.utils.Utility.*;

public class PeerClient implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger("PEER_CLIENT");
    private Selector selector;
    private final List<PeerInfo> peerInfos;

    public PeerClient(List<PeerInfo> infos) {
        this.peerInfos = infos;
    }

    @Override
    public void run() {
        LOGGER.info("Peer client started");
        try {
            selector = Selector.open();
            while (!isClientDone()) {
                registerPeerInfos();
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();
                //LOGGER.debug("Mapper: {}", bitsetInfo.toString());
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isConnectable()) {
                        handleConnectable(key);
                    } else if (key.isWritable()) {
                        handleWritable(key);
                    } else if (key.isReadable()) {
                        handleReadable(key);
                    }
                }
            }
            handleEndWorkForClient();
        } catch (IOException e) {
            LOGGER.error("Client work error");
            try {
                selector.close();
            } catch (IOException ex) {
                throw new TorrentException(ex);
            }
            throw new TorrentException(e);
        }
        LOGGER.info("All pieces for port:{} downloaded", PORT);

    }

    private void handleEndWorkForClient() {
        LOGGER.info("Finishing client work...");
        for (SelectionKey key : selector.keys()) {
            PeerInfo info = (PeerInfo) key.attachment();
            PeerCondition condition = info.getCondition();
            if (condition != PeerCondition.NO_CONNECT && condition != PeerCondition.UNUSED) {
                sendNotInterested(key);
                try {
                    key.channel().close();
                    key.cancel();
                    info.setCondition(PeerCondition.NOT_INTERESTED);
                } catch (IOException e) {
                    LOGGER.error("Error closing channel for peer: {}", info.getPort(), e);
                }
            }
        }
        try {
            selector.close();
        } catch (IOException e) {
            LOGGER.error("Error closing selector", e);
        }
    }

    private void sendNotInterested(SelectionKey key) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(5);
            buffer.putInt(1);
            buffer.put(MessageType.NOT_INTERESTED.getId());
            buffer.flip();
            SocketChannel channel = (SocketChannel) key.channel();
            channel.write(buffer);
            LOGGER.debug("Sent 'not interested' to peer on port: {}", ((PeerInfo) key.attachment()).getPort());
        } catch (IOException e) {
            LOGGER.error("Error sending 'not interested' message", e);
        }
    }

    private void handleReadable(SelectionKey key) throws IOException {
        PeerInfo info = (PeerInfo) key.attachment();
        PeerCondition condition = info.getCondition();
        try {
            switch (condition) {
                case WAITING_HANDSHAKE_RESPONSE -> receiveHandshake(key, info);
                case WAITING_BITFIELD -> receiveBitfield(key, info);
                case WAITING_PIECE -> receivePiece(key, info);
            }
        } catch (IOException | TorrentException e) {
            LOGGER.error("Error while readable handling. Disconnect port: {}", info.getPort());
            key.channel().close();
            key.cancel();
            info.setCondition(PeerCondition.NO_CONNECT);
        }
    }

    private void receivePiece(SelectionKey key, PeerInfo info) throws IOException {
        LOGGER.info("Receiving piece from port:{}", info.getPort());
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(13);
        int read = socketChannel.read(buffer);
        buffer.flip();
        int length = buffer.getInt();
        byte id = buffer.get();
        int index = buffer.getInt();
        int offset = buffer.getInt();
        buffer = ByteBuffer.allocate(length - 13);
        while (read < buffer.capacity()) {
            read += socketChannel.read(buffer);
        }
        LOGGER.debug("RECEIVED PIECE  : {}", index);
        if (!checkPieceHash(index, buffer.array())) {
            LOGGER.debug("Hashes did not match");
            throw new TorrentException("Piece hash mismatch");
        }
        savePieceData(index, offset, buffer.array());
        info.setCondition(PeerCondition.SEND_BITFIELD);
        key.interestOps(SelectionKey.OP_WRITE);
    }

    private boolean checkPieceHash(int index, byte[] block) {
        if (index == SIZE - 1) return true;
        HashCode hashCode = Hashing.sha1().hashBytes(block);
        byte[] expectedHash = Utility.getHashCodeByIndex(index);
        return Arrays.equals(hashCode.asBytes(), expectedHash);
    }

    private void savePieceData(int index, int offset, byte[] block) throws IOException {
        long position = (long) index * PIECE_LENGTH + offset;
        DEST_FILE.seek(position);
        DEST_FILE.write(block);
        BITFIELD.set(index);
        LOGGER.info("Piece data saved at index {}, offset {}", index, offset);
        LOGGER.info("Current bitfield for port{}: {}", PORT, BITFIELD.toString());
    }

    private void receiveBitfield(SelectionKey key, PeerInfo info) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(5 + SIZE);
        SocketChannel socketChannel = (SocketChannel) key.channel();
        int read = socketChannel.read(buffer);
        if (read < 0) {
            throw new TorrentException("Socket channel closed. No bytes read from bitfield message");
        }
        buffer.flip();
        int len = buffer.getInt();
        byte id = buffer.get();
        byte[] bitfield = new byte[len - 1];
        buffer.get(bitfield);
        bitsetInfo.add(byteArrayToBitset(bitfield), info.getPort());
        LOGGER.debug("Mapper:{}: {}", PORT, bitsetInfo.toString());
        key.interestOps(SelectionKey.OP_WRITE);
        info.setCondition(PeerCondition.SEND_REQUEST);
    }

    private void receiveHandshake(SelectionKey key, PeerInfo info) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(HANDSHAKE_SIZE);
        SocketChannel socketChannel = (SocketChannel) key.channel();
        int read = socketChannel.read(buffer);
        if (read < 0) {
            throw new TorrentException("Socket channel closed. No bytes read from handshake message");
        }
        buffer.flip();
        byte pstrlen = buffer.get();
        if (pstrlen != 19) {
            LOGGER.error("Wrong byte");
            throw new TorrentException("Wrong byte from handshake:" + PORT);
        }

        byte[] pstr = new byte[19];
        buffer.get(pstr);
        if (!("BitTorrent protocol").equals(new String(pstr))) {
            LOGGER.error("Wrong protocol received from peer:{}", info.getPort());
            throw new TorrentException("Drop connection cos of invalid protocol");
        }
        buffer.position(28);
        byte[] infoHash = new byte[20];
        buffer.get(infoHash);
        if (!Arrays.equals(infoHash, INFO_HASH)) {
            LOGGER.error("Wrong info hash received from peer:{}", info.getPort());
            throw new TorrentException("Drop connection cos of invalid info hash");
        }
        byte[] peerId = new byte[20];
        buffer.get(peerId);
        if (!Arrays.equals(peerId, info.getPeerId())) {
            LOGGER.error("Wrong peer id received from peer:{}", info.getPort());
            throw new TorrentException("Drop connection cos of invalid peer id");
        }
        key.interestOps(SelectionKey.OP_WRITE);
        info.setCondition(PeerCondition.SEND_BITFIELD);
    }

    private void handleWritable(SelectionKey key) throws IOException {
        PeerInfo info = (PeerInfo) key.attachment();
        PeerCondition condition = info.getCondition();
        try {
            switch (condition) {
                case CONNECTED -> sendHandshake(key, info);
                case SEND_BITFIELD -> sendBitfield(key, info);
                case SEND_REQUEST -> sendRequest(key, info);
            }
        } catch (IOException e) {
            LOGGER.error("Error while writable handling. Disconnect port: {}", info.getPort());
            key.channel().close();
            key.cancel();
            info.setCondition(PeerCondition.NO_CONNECT);
        }
    }

    private void sendRequest(SelectionKey key, PeerInfo info) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(17);
        buffer.putInt((byte) 13);
        buffer.put(MessageType.REQUEST.getId());
        SocketChannel socketChannel = (SocketChannel) key.channel();
        for (int i = 0; i < SIZE; i++) {
            List<Integer> list = bitsetInfo.getPortForPieceIndex(i);
            if (!BITFIELD.get(i) && list != null && list.contains(info.getPort())) {
                buffer.putInt(i);
                buffer.putInt(0);
                buffer.putInt(getPieceLength(i)); // FIXME
                buffer.flip();
                socketChannel.write(buffer);
                key.interestOps(SelectionKey.OP_READ);
                info.setCondition(PeerCondition.WAITING_PIECE);
                LOGGER.debug("Send request for peer:{} for piece {}", info.getPort(), i);
                break;
            }
        }
    }

    private void sendBitfield(SelectionKey key, PeerInfo info) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(SIZE + 1 + 4);
        buffer.putInt(SIZE + 1);
        buffer.put(MessageType.BITFIELD.getId());
        byte[] bitfieldBytes = bitfieldToByteArray(BITFIELD);
        if (bitfieldBytes.length != SIZE) {
            LOGGER.error("Bitfield size is incorrect. Expected size: {}, actual size: {}", SIZE, bitfieldBytes.length);
            throw new TorrentException("Error with bitfield");
        }
        buffer.put(bitfieldBytes);
        buffer.flip();
        SocketChannel channel = (SocketChannel) key.channel();
        channel.write(buffer);
        key.interestOps(SelectionKey.OP_READ);
        info.setCondition(PeerCondition.WAITING_BITFIELD);
        LOGGER.info("BitsetMapper: {}", bitsetInfo.toString());
    }

    private void sendHandshake(SelectionKey key, PeerInfo info) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(HANDSHAKE_SIZE);
        buffer.put((byte) 19);
        buffer.put("BitTorrent protocol".getBytes());
        buffer.put(new byte[8]);
        buffer.put(Utility.INFO_HASH);
        buffer.put(PEER_ID);
        buffer.flip();
        SocketChannel channel = (SocketChannel) key.channel();
        channel.write(buffer);
        key.interestOps(SelectionKey.OP_READ);
        info.setCondition(PeerCondition.WAITING_HANDSHAKE_RESPONSE);
        LOGGER.debug("Handshake message send successfully to server:{}", info.getPort());
    }

    private void handleConnectable(SelectionKey key) throws IOException {
        PeerInfo info = (PeerInfo) key.attachment();
        SocketChannel channel = (SocketChannel) key.channel();
        try {
            channel.finishConnect();
        } catch (IOException e) {
            key.cancel();
            channel.close();
            info.setChannel(null);
            info.setCondition(PeerCondition.NO_CONNECT);
            return;
        }
        info.setCondition(PeerCondition.CONNECTED);
        key.interestOps(SelectionKey.OP_WRITE);
        LOGGER.debug("Connection finished with {}", info.getPort());
    }

    private void registerPeerInfos() throws IOException {
        for (PeerInfo info : peerInfos) {
            if (info.getCondition() != PeerCondition.NO_CONNECT) {
                continue;
            }
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(info.getIp(), info.getPort()));
            socketChannel.register(selector, SelectionKey.OP_CONNECT, info);
            info.setCondition(PeerCondition.REGISTERED);
            info.setChannel(socketChannel);
        }
    }

    private boolean isClientDone() {
        return BITFIELD.cardinality() == SIZE;
    }
}
