package nsu_laboratory.utils;


import com.dampcake.bencode.BencodeInputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dampcake.bencode.BencodeOutputStream;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BencodeParser {
    Logger LOGGER = LogManager.getLogger();
    private final String announce;
    private List<List<String>> announceList;
    private final long creationDate;
    private final String comment;
    private final String createdBy;
    private final long fileLength;
    private final long pieceLength;
    private final String nameFile;
    private final int numberOfPieces;
    private final List<byte[]> pieceHashes;
    byte[] infoHash;
    String infoHashString;

    public BencodeParser(File torrentFile) {
        try {
            FileInputStream fileInputStream = new FileInputStream(torrentFile);
            BencodeInputStream bencodeInputStream = new BencodeInputStream(fileInputStream, Charset.defaultCharset(), true);
            Map<String, Object> torrentData = bencodeInputStream.readDictionary();
            announce = decodeString((ByteBuffer) torrentData.get("announce"));
            Object announceListObj = torrentData.get("announce-list");
            if (announceListObj instanceof List) {
                announceList = (List<List<String>>) announceListObj;
            }
            creationDate = (Long) torrentData.getOrDefault("creation date", 0L);
            comment = decodeString((ByteBuffer) torrentData.get("comment"));
            createdBy = decodeString((ByteBuffer) torrentData.get("created by"));
            Map<String, ?> info = (Map<String, ?>) torrentData.get("info");
            nameFile = decodeString((ByteBuffer) info.get("name"));
            fileLength = (Long) info.get("length");
            pieceLength = ((Long) info.get("piece length"));
            calculateInfoHash(info);
            numberOfPieces = (int) Math.ceil((double) fileLength / pieceLength);
            ByteBuffer piecesBuffer = (ByteBuffer) info.get("pieces");
            byte[] piecesBytes = new byte[piecesBuffer.remaining()];
            piecesBuffer.get(piecesBytes);
            pieceHashes = new ArrayList<>();
            for (int i = 0; i < piecesBytes.length; i += 20) {
                byte[] hash = new byte[20];
                for (int j = 0; j < 20; j++) {
                    hash[j] = (byte) (piecesBytes[i + j] & 0xFF);
                }
                pieceHashes.add(hash);
            }
            bencodeInputStream.close();
            LOGGER.info("{}", fileLength);
        } catch (IOException e) {
            LOGGER.error("Get problems with bencoding the file");
            throw new TorrentException(e);
        }
    }

    private void calculateInfoHash(Map<String, ?> info) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BencodeOutputStream bencodeOutputStream = new BencodeOutputStream(outputStream);
            bencodeOutputStream.writeDictionary(info);
            byte[] serializedInfo = outputStream.toByteArray();
            HashCode hashCode = Hashing.sha1().hashBytes(serializedInfo);
            infoHash = hashCode.asBytes();
            infoHashString = hashCode.toString();
            LOGGER.debug("Info hash: {} {}", infoHash, infoHash.length);
            LOGGER.debug("String info hash: {}", infoHashString);
        } catch (Exception e) {
            LOGGER.error("No info hash");
            throw new TorrentException("Info hash calculating error with stream");
        }
    }

    private String decodeString(ByteBuffer buffer) {
        if (buffer == null) {
            return null;
        }
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
        try {
            return decoder.decode(buffer).toString();
        } catch (IOException e) {
            LOGGER.error("String decode error");
            return null;
        }
    }

    /**
     * @return the size of a piece
     */
    public int getPieceLength() {
        LOGGER.debug("Getting piece length: {}", pieceLength);
        return (int) pieceLength;
    }

    /**
     * @return the size of piece. Sometimes the last piece may be smaller, so it is calculated separately
     */
    public long getCurrentPieceLength(int index) {
        long currPieceLength;
        if (index < numberOfPieces - 1) {
            currPieceLength = pieceLength;
        } else {
            currPieceLength = fileLength - (pieceLength * (numberOfPieces - 1));
        }
        LOGGER.debug("Getting piece length by index: {} | length: {}", index, currPieceLength);
        return currPieceLength;
    }

    public String getAnnounce() {
        LOGGER.debug("Getting announce: {}", announce);
        return announce;
    }

    public List<List<String>> getAnnounceList() {
        LOGGER.debug("Getting announce list: {}", announceList);
        return announceList;
    }

    public long getCreationDate() {
        LOGGER.debug("Getting creation date: {}", creationDate);
        return creationDate;
    }

    public String getComment() {
        LOGGER.debug("Getting comment: {}", comment);
        return comment;
    }

    public String getCreatedBy() {
        LOGGER.debug("Getting created by: {}", createdBy);
        return createdBy;
    }

    public long getFileLength() {
        LOGGER.debug("Getting file length: {}", fileLength);
        return fileLength;
    }

    public String getFileName() {
        LOGGER.debug("Getting file name: {}", nameFile);
        return nameFile;
    }

    /**
     * @return byte array with current piece hash
     */
    public byte[] getPieceHashes(int index) {
        return pieceHashes.get(index);
    }

    public int getNumberOfPieces() {
        LOGGER.debug("Getting number of pieces: {} ", numberOfPieces);
        return numberOfPieces;
    }

    public int getHashesSize() {
        LOGGER.debug("Getting number of hashes: {}", pieceHashes.size());
        return pieceHashes.size();
    }

    public byte[] getInfoHash() {
        return infoHash;
    }
}