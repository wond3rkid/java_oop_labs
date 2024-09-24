package nsu_laboratory.utils;

public class TorrentException extends RuntimeException {
    public TorrentException(String message) {
        super(message);
    }

    public TorrentException(String message, Throwable cause) {
        super(message, cause);
    }

    public TorrentException(Throwable cause) {
        super(cause);
    }
}
