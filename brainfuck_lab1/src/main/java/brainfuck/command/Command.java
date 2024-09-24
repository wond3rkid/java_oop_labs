package brainfuck.command;

import brainfuck.InterpreterContext;

public interface Command {
    void execute(InterpreterContext context);

    default String getName() {
        return this.getClass().getSimpleName();
    }

    default void updateCmdPointer(InterpreterContext context) {
        context.increaseCmdPointer();
    }
}