package brainfuck.command;

import brainfuck.InterpreterContext;

public class StartLoopCommand implements Command {
    @Override
    public void execute(InterpreterContext context) {
        context.startLoop();
    }
}