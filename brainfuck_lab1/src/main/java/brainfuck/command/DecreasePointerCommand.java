package brainfuck.command;

import brainfuck.InterpreterContext;

public class DecreasePointerCommand implements Command {
    @Override
    public void execute(InterpreterContext context) {
        context.decreaseMemoryPointer();
    }
}