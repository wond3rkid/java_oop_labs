package brainfuck.command;

import brainfuck.InterpreterContext;

public class DecreaseElementCommand implements Command {
    @Override
    public void execute(InterpreterContext context) {
        context.decreaseCurrentElement();
    }
}