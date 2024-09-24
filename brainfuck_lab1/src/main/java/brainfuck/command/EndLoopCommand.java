package brainfuck.command;

import brainfuck.InterpreterContext;

public class EndLoopCommand implements Command {
    boolean isLoopDone = false;

    @Override
    public void execute(InterpreterContext context) {
        isLoopDone = context.isLoopDone();
    }

    @Override
    public void updateCmdPointer(InterpreterContext context) {
        if (isLoopDone) {
            context.doneLoopHandler();
        } else {
            context.repeatLoopHandler();
        }
    }
}