package brainfuck.command;

import brainfuck.InterpreterContext;
import brainfuck.exception.InterpreterException;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputCommand implements Command {
    @Override
    public void execute(InterpreterContext context) {
        try (Scanner in = new Scanner(System.in);) {
            byte inputByte = (byte) in.next().charAt(0);
            context.setCurrentElement(inputByte);
        } catch (NoSuchElementException err) {
            throw new InterpreterException("Error with input. Try again");
        }
    }
}