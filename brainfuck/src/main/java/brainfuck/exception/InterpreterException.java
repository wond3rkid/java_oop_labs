package brainfuck.exception;

public class InterpreterException extends RuntimeException {
    public InterpreterException(String errorMessage) {
        super(errorMessage);
    }
}