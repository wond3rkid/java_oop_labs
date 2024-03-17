package brainfuck.command;


import brainfuck.InterpreterContext;

public class StartLoopCommand implements Command {
    @Override
    public void execute(InterpreterContext context) {
        context.startLoop();
    }

    /*
    default т.к мы сделали всю подготоку с указателями в функции startloop и дальше просто идем по коммандам
    public void updateCmdPointer(InterpreterContext context) {}
    */
}