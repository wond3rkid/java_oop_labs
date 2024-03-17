package brainfuck.command;

import brainfuck.InterpreterContext;

public class InputCommand implements Command {
    @Override
    public void execute(InterpreterContext context) {
        context.inputSymbol();
    }
}