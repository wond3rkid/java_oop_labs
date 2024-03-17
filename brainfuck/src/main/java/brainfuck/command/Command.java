package brainfuck.command;

import brainfuck.InterpreterContext;

public interface Command {
    void execute(InterpreterContext context);
    // ???
    default void updateCmdPointer(InterpreterContext context) {
        context.increaseCmdPointer();
    }
}