package nsu_laboratory.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;

public class Utility {
    private static final Logger LOGGER = LogManager.getLogger("Config");
    public static URI TORRENT_PATH;
    public static URI REAL_PATH;
    private static URI DEST_PATH;
    static BencodeParser PARSER;
    public static final int SIZE;
    public static int PORT;
    public static byte[] INFO_HASH;
    public static int PIECE_LENGTH;
    public static RandomAccessFile FILE, DEST_FILE;
    public static int HANDSHAKE_SIZE = 68;
    public static int BUFFER = 256;
    public static BitSet BITFIELD;
    public static byte[] PEER_ID;
    public static BitsetMapper bitsetInfo;

    static {
        try {
            TORRENT_PATH = new File("src/main/resources/WarAndPeace.txt.torrent").getAbsoluteFile().toURI();
            REAL_PATH = new File("src/main/resources/WarAndPeace.txt").getAbsoluteFile().toURI();
            FILE = new RandomAccessFile(new File(REAL_PATH).getPath(), "r");
            LOGGER.debug("Torrent path: {}", TORRENT_PATH);
            LOGGER.debug("Real path: {}", REAL_PATH);
            PARSER = new BencodeParser(new File(TORRENT_PATH));
            SIZE = PARSER.getNumberOfPieces();
            INFO_HASH = PARSER.getInfoHash();
            PIECE_LENGTH = PARSER.getPieceLength();
            BITFIELD = new BitSet(SIZE);
            bitsetInfo = new BitsetMapper();
        } catch (Exception e) {
            LOGGER.fatal("Error initializing parser: ", e);
            throw new TorrentException(e.getMessage());
        }
    }

    public static void setPort(int port) {
        PORT = port;
        DEST_PATH = new File("src/main/resources/WarPeace" + port + ".txt").getAbsoluteFile().toURI();

        try {
            DEST_FILE = new RandomAccessFile(new File(DEST_PATH).getPath(), "rw");
        } catch (FileNotFoundException e) {
            throw new TorrentException(e);
        }
    }

    public static byte[] getHashCodeByIndex(int index) {
        return PARSER.getPieceHashes(index);
    }

    public static String getStringByteBuffer(ByteBuffer buffer) {
        buffer.mark();
        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray);
        buffer.reset();
        return Arrays.toString(byteArray);
    }

    public static void setPeerHash(byte[] peerHash) {
        PEER_ID = peerHash;
    }

    public static void updateBitfield(BitSet bitfield) {
        BITFIELD = bitfield;
        bitsetInfo.add(BITFIELD, PORT);
    }

    public static byte[] bitfieldToByteArray(BitSet bitfield) {
        byte[] byteArray = new byte[SIZE];
        for (int i = 0; i < SIZE; i++) {
            if (bitfield.get(i)) {
                byteArray[i] = 1;
            } else {
                byteArray[i] = 0;
            }
        }
        return byteArray;
    }

    public static BitSet byteArrayToBitset(byte[] byteArray) {
        BitSet bitset = new BitSet();
        for (int i = 0; i < byteArray.length; i++) {
            if (byteArray[i] == 1) {
                bitset.set(i);
            }
        }
        return bitset;
    }

    public static void downloadOurPieces() throws IOException {
        for (int i = 0; i < SIZE; i++) {
            if (BITFIELD.get(i)) {
                int offset = i * PIECE_LENGTH;
                DEST_FILE.seek(offset);
                byte[] data;
                if (i != SIZE - 1) {
                    int size = (int) PARSER.getCurrentPieceLength(i);
                    data = new byte[size];
                    FILE.read(data, 0, PIECE_LENGTH);
                } else {
                    int remain = (int) PARSER.getCurrentPieceLength(i);
                    data = new byte[remain];
                    FILE.read(data, 0, remain);
                }
                DEST_FILE.write(data);
            }
        }
        LOGGER.debug("All my pieces here");
    }

    public static int getPieceLength(int index) {
        return (int) PARSER.getCurrentPieceLength(index);
    }
}
