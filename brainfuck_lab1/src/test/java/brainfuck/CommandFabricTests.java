package brainfuck;

import brainfuck.command.Command;
import brainfuck.command.CommandFabric;
import brainfuck.command.IncreaseElementCommand;
import brainfuck.exception.FabricException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandFabricTests {
    @Test
    public void createFabric() {
        CommandFabric commandFabric = new CommandFabric();
        assertFalse(commandFabric.isCommandRegistered('+'));
        assertFalse(commandFabric.isCommandRegistered('-'));
        assertFalse(commandFabric.isCommandRegistered('['));
        assertFalse(commandFabric.isCommandRegistered(']'));
        assertFalse(commandFabric.isCommandRegistered('>'));
        assertFalse(commandFabric.isCommandRegistered('<'));
        assertFalse(commandFabric.isCommandRegistered('.'));
        assertFalse(commandFabric.isCommandRegistered(','));
    }

    @Test
    public void registration() {
        CommandFabric commandFabric = new CommandFabric();
        boolean testReg = commandFabric.registry('+');
        assertTrue(commandFabric.isCommandRegistered('+'));
        assertTrue(testReg);
    }

    @Test
    public void getInstance() {
        CommandFabric commandFabric = new CommandFabric();
        Assertions.assertThrows(FabricException.class, () -> {
            commandFabric.getCommandInstance('+');
        });
        IncreaseElementCommand increase = new IncreaseElementCommand();
        commandFabric.registry('+');
        Command instance = commandFabric.getCommandInstance('+');
        assertEquals(instance.getClass(), increase.getClass());
    }
    @Test
    public void getExceptionInput() {
        String badCode = "{";
        Interpreter interpreter = new Interpreter(badCode);
        Assertions.assertThrows(FabricException.class, interpreter::run);
    }

}