package brainfuck;

import brainfuck.exception.FabricException;
import brainfuck.exception.InterpreterException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("start main");
        logger.error("error main");
        logger.debug("debug main");
        logger.fatal("fatal main");
        try {
            Interpreter interpreter = new Interpreter();
            interpreter.run();
        } catch (InterpreterException | NullPointerException | FabricException err) {
            logger.error("An error occurred while interpreter was working:");
            System.err.println(err.getMessage());
        }
    }
}