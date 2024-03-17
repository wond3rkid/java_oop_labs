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

    public CommandFabric() {
    }

    public boolean registry(char sym) {
        Class<? extends Command> currClass = registeredCommands.get(sym);
        try {
            if (currClass == null) {
                String className = getClassNameFromProperties(sym);
                if (className != null) {
                    currClass = (Class<? extends Command>) Class.forName(className);
                    registeredCommands.put(sym, currClass);
                    return true;
                }
            }
        } catch (ClassNotFoundException err) {
            logger.error("You wrote the wrong command:");
            throw new FabricException("Class was not found. Brainfuck does not support this operation");
        }
        return false;
    }

    public boolean isCommandRegistered(char sym) {
        return registeredCommands.containsKey(sym);
    }

    private String getClassNameFromProperties(char sym) {
        try {
            Properties properties = new Properties();
            InputStream input = CommandFabric.class.getClassLoader().getResourceAsStream("brainfuck_command.properties");
            properties.load(input);
            assert input != null;
            input.close();
            return properties.getProperty(Character.toString(sym));

        } catch (IOException | NullPointerException err) {
            logger.error("Error with properties file. Interpreter's run failed.");
            throw new FabricException("You did not get class name from property-file.");
        }
    }

    public Command getCommandInstance(char command) {
        try {
            return registeredCommands.get(command).getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException err) {
            logger.error("Error with getting instance of the current command. Interpreter failed:");
            throw new FabricException("You did not get command instance");
        }
    }
}