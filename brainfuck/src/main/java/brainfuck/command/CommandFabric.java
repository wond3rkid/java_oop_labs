package brainfuck.command;

import brainfuck.InterpreterContext;
import brainfuck.exception.FabricException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Properties;

public class CommandFabric {
    private static final Logger logger = LogManager.getLogger(CommandFabric.class);
    private final HashMap<Character, Class<? extends Command>> registeredCommands = new HashMap<>();

    public boolean registry(char sym) {
        Class<? extends Command> currClass = registeredCommands.get(sym);
        logger.info("The command " + sym + " is being registered at the factory.");
        try {
            if (currClass == null) {
                String className = getClassNameFromProperties(sym);
                if (className != null) {
                    currClass = (Class<? extends Command>) Class.forName(className);
                    logger.info("Registration completed successfully for " + sym + " command.");
                    registeredCommands.put(sym, currClass);
                    return true;
                }
            }
        } catch (ClassNotFoundException err) {
            logger.info("Registration was not successful for " + sym + " command.");
            logger.error("The interpreter has terminated due to an error:");
            throw new FabricException("Class was not found. Brainfuck does not support this operation");
        }
        return false;
    }

    public boolean isCommandRegistered(char sym) {
        logger.info("Checking whether the command is registered in the factory.");
        return registeredCommands.containsKey(sym);
    }

    private String getClassNameFromProperties(char sym) {
        logger.info("Start get class name from property file.");
        try (InputStream input = CommandFabric.class.getClassLoader().getResourceAsStream("brainfuck_command.properties");) {
            Properties properties = new Properties();
            properties.load(input);
            return properties.getProperty(Character.toString(sym));
        } catch (IOException e) {
            logger.error("Error with properties file. The interpreter has terminated due to an error:");
            throw new FabricException("You did not get class name from property-file.");
        }
    }

    public Command getCommandInstance(char command) {
        logger.info("Start getting command instance from the Command Factory:");
        try {
            return registeredCommands.get(command).getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException err) {
            logger.error("Error with getting instance of the current command. The interpreter has terminated due to an error:");
            throw new FabricException("You did not get command instance.");
        }
    }
}