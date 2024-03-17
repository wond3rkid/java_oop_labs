package brainfuck;

import brainfuck.command.Command;
import brainfuck.command.CommandFabric;
import brainfuck.exception.InterpreterException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;


public class Interpreter {
    private static final Logger logger = LogManager.getLogger(Interpreter.class);
    private final CommandFabric commandFabric = new CommandFabric();
    private final InterpreterContext context = new InterpreterContext();
    String code;

    Interpreter(String input) {
        this.code = input;
    }

    Interpreter() {
        Scanner in = new Scanner(System.in);
        this.code = in.next();
        in.close();
    }

    public void run() {
        int i = context.getCommandPointer();
        while (i < code.length()) {
            if (!commandFabric.isCommandRegistered(code.charAt(i))) {
                boolean regCmdFlag = commandFabric.registry(code.charAt(i));
                if (!regCmdFlag) {
                    logger.error("Fatal error of Interpreter:");
                    throw new InterpreterException("You are trying to execute not-existing command. Try again");
                }
            }
            executeCommand(code.charAt(i));
            i = context.getCommandPointer();
        }
    }

    private void executeCommand(char command) {
        Command current = commandFabric.getCommandInstance(command);
        current.execute(context);
        current.updateCmdPointer(context);
    }

    public InterpreterContext getContext() {
        return context;
    }
}