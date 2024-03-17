package brainfuck;

import brainfuck.command.Command;
import brainfuck.command.CommandFabric;
import brainfuck.command.IncreaseElementCommand;
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
        Command instance = commandFabric.getCommandInstance('+');
        IncreaseElementCommand increase = new IncreaseElementCommand();
        assertNull(instance);
        commandFabric.registry('+');
        instance = commandFabric.getCommandInstance('+');
        assertEquals(instance.getClass(), increase.getClass());
    }
}