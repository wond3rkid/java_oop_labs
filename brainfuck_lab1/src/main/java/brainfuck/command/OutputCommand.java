package brainfuck.command;

import brainfuck.InterpreterContext;

public class OutputCommand implements Command {
    @Override
    public void execute(InterpreterContext context) {
        context.outputHandler();
    }
}