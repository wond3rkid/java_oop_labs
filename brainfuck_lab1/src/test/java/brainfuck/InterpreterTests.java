package brainfuck;

import brainfuck.exception.InterpreterException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InterpreterTests {
    @Test
    public void createInterpreter() {
        InterpreterContext interpreterContext = new InterpreterContext();
        assertEquals(interpreterContext.getMemoryPointer(), 0);
        assertEquals(interpreterContext.getCommandPointer(), 0);
        assertEquals(interpreterContext.getElementByIndex(0), 0);
    }

    @Test
    public void increaseElement() {
        String testCode = "++++";
        Interpreter interpreter = new Interpreter(testCode);
        interpreter.run();
        assertEquals(interpreter.getContext().getCurrentElement(), 4);
    }

    @Test
    public void decreaseElement() {
        String testCode = "-";
        Interpreter interpreter = new Interpreter(testCode);
        interpreter.run();
        assertEquals(interpreter.getContext().getCurrentElement(), Byte.MAX_VALUE);
    }

    @Test
    public void increasePointer() {
        String testCode = ">>>>";
        Interpreter interpreter = new Interpreter(testCode);
        interpreter.run();
        assertEquals(interpreter.getContext().getMemoryPointer(), 4);
    }

    @Test
    public void decreasePointer() {
        String testCode = ">>>><<<<";
        Interpreter interpreter = new Interpreter(testCode);
        interpreter.run();
        assertEquals(interpreter.getContext().getMemoryPointer(), 0);
    }

    @Test
    public void outputElement() {
        String hello = "++++++++++[>+++++++>++++++++++>+++<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.";
        Interpreter interpreter = new Interpreter(hello);
        interpreter.run();
        assertEquals("Hello World!", interpreter.getContext().getOutput());
    }

    @Test
    public void getExceptionLoop() {
        String badCode = "]";
        Interpreter interpreter = new Interpreter(badCode);
        Assertions.assertThrows(InterpreterException.class, interpreter::run);
    }

    @Test
    public void getExceptionNotendedLoop() {
        String badCode = "[++++++++++++";
        Interpreter interpreter = new Interpreter(badCode);
        Assertions.assertThrows(InterpreterException.class, interpreter::run);
    }
}