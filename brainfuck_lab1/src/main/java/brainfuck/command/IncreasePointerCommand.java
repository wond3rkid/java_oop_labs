package brainfuck.command;

import brainfuck.InterpreterContext;

public class IncreasePointerCommand implements Command {
    @Override
    public void execute(InterpreterContext context) {
        context.increaseMemoryPointer();
    }
}