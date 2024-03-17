package brainfuck.command;

import brainfuck.InterpreterContext;
import brainfuck.exception.InterpreterException;

public class OutputCommand implements Command {
    @Override
    public void execute(InterpreterContext context) {
        /* if (context.getElementByIndex(context.getMemoryPointer()) <= 32) {
            throw new InterpreterException("You can not print this symbol " + context.getElementByIndex(context.getMemoryPointer()));
        }*/
        System.out.print(context.getCurrentElement() + " ");
    }
}