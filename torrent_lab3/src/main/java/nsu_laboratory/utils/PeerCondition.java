package nsu_laboratory.utils;

public enum PeerCondition {
    NO_CONNECT,
    REGISTERED,
    CONNECTED,
    SEND_HANDSHAKE,
    WAITING_HANDSHAKE_RESPONSE,
    SEND_BITFIELD,
    WAITING_BITFIELD,
    SEND_REQUEST,
    WAITING_PIECE,


    CHOKE,
    UNCHOKE,
    INTERESTED,
    NOT_INTERESTED,
    HAVE,
    CANCEL,
    UNUSED,
}
