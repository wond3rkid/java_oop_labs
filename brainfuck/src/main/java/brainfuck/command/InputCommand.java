package brainfuck.command;

import brainfuck.InterpreterContext;
import brainfuck.exception.InterpreterException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputCommand implements Command {
    private static final Logger logger = LogManager.getLogger(InputCommand.class);
    @Override
    public void execute(InterpreterContext context) {
        try (Scanner in = new Scanner(System.in);) {
            byte inputByte = (byte) in.next().charAt(0);
            context.setCurrentElement(inputByte);
        } catch (NoSuchElementException err) {
            logger.error("You can not input this symbol");
            throw new InterpreterException("Error with input. Try again");
        }
    }
}