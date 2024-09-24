package nsu_laboratory.utils;

import com.google.common.hash.Hashing;

import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public class PeerInfo {
    private String ip;
    private int port;
    private BitSet bitfield;
    private byte[] peerId;
    private SocketChannel channel;
    private PeerCondition condition;

    public PeerInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.bitfield = new BitSet();
        calculateHash();
        condition = PeerCondition.NO_CONNECT;
    }

    public int getCountOfPieces() {
        return bitfield.cardinality();
    }

    private void calculateHash() {
        peerId = Hashing.sha1().hashString(ip + port, StandardCharsets.UTF_8).asBytes();
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public BitSet getBitfield() {
        return bitfield;
    }

    public void setBitfield(BitSet bitfield) {
        this.bitfield = bitfield;
    }

    public byte[] getPeerId() {
        return peerId;
    }

    public void setBitByIndex(int index) {
        bitfield.set(index);
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public void setChannel(SocketChannel channel) {
        this.channel = channel;
    }

    public PeerCondition getCondition() {
        return condition;
    }

    public void setCondition(PeerCondition condition) {
        this.condition = condition;
    }
}
