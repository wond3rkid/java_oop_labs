package brainfuck.command;

import brainfuck.InterpreterContext;

public class IncreaseElementCommand implements Command {
    @Override
    public void execute(InterpreterContext context) {
        context.increaseCurrentElement();
    }
}