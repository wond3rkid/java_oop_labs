package brainfuck.exception;

public class FabricException extends RuntimeException {
    public FabricException(String errorMessage) {
        super(errorMessage);
    }
}