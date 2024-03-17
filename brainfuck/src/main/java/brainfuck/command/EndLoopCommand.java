package brainfuck.command;

import brainfuck.InterpreterContext;

public class EndLoopCommand implements Command {

    boolean isLoopDone = false;

    @Override
    public void execute(InterpreterContext context) {
        if (context.isLoopDone()) {
            isLoopDone = true;
        } else {
            isLoopDone = false;
        }
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