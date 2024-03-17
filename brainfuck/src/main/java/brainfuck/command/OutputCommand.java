package brainfuck.command;

import brainfuck.InterpreterContext;
import brainfuck.exception.InterpreterException;

public class OutputCommand implements Command {
    @Override
    public void execute(InterpreterContext context) {
        context.printCurrentElement();
    }
}