package nsu_laboratory.utils;

public enum MessageType {
    CHOKE(0),
    UNCHOKE(1),
    INTERESTED(2),
    NOT_INTERESTED(3),
    HAVE(4),
    BITFIELD(5),
    REQUEST(6),
    PIECE(7),
    CANCEL(8),
    PORT(9),
    KEEP_ALIVE(10);

    private final byte id;

    MessageType(int id) {
        this.id = (byte) id;
    }

    public byte getId() {
        return id;
    }
}
