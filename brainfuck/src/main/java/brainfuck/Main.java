package brainfuck;

import brainfuck.exception.FabricException;
import brainfuck.exception.InterpreterException;

public class Main {
    public static void main(String[] args) {
        try {
            Interpreter interpreter = new Interpreter();
            interpreter.run();
        } catch (InterpreterException | NullPointerException | FabricException err) {
            System.err.println(err.getMessage());
        }
    }
}