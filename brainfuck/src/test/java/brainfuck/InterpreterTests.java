package brainfuck;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertTrue(interpreter.getContext().getOutput().equals("Hello World!"));
    }

}