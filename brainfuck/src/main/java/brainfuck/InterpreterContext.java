
package brainfuck;

import brainfuck.exception.InterpreterException;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InterpreterContext {
    private static final Logger logger = LogManager.getLogger(InterpreterContext.class);
    static final int ARRAY_SIZE = 65535;
    int cmdPointer;
    int memoryPointer;
    byte[] memory;
    Stack<Integer> stackCmd = new Stack<>();
    Stack<Integer> stackMmr = new Stack<>();
    Scanner in = new Scanner(System.in);

    InterpreterContext() {
        this.cmdPointer = 0;
        this.memoryPointer = 0;
        this.memory = new byte[ARRAY_SIZE];
        Arrays.fill(memory, (byte) 0);
    }

    public void increaseCmdPointer() {
        this.cmdPointer += 1;
    }

    public void printCurrentElement() {
        if (memory[memoryPointer] < 32) {
            logger.error("You can not print this symbol, it's ASCII code: " + memory[memoryPointer]);
            throw new InterpreterException("Interpreter failed.");
        } else {
            System.out.print((char) memory[memoryPointer]);
        }
    }

    public byte getCurrentElement() {
        return memory[memoryPointer];
    }

    public void inputSymbol() {
        System.out.println("You have to enter the character in the console: ");
        try {
            byte inputByte = (byte) in.next().charAt(0);
            this.setCurrentElement(inputByte);
        } catch (NoSuchElementException err) {
            logger.error("You did not write the symbol");
            throw new InterpreterException("Error with input. Try again.");
        }
    }

    public void setCommandPointer(int pointer) {
        this.cmdPointer = pointer;
    }

    public void increaseMemoryPointer() {
        if (memoryPointer == ARRAY_SIZE - 1) {
            logger.error("Fatal error of Interpreter");
            throw new InterpreterException("Index of array can not be more than maximum size: " + ARRAY_SIZE);
        } else {
            this.memoryPointer++;
        }

    }

    public void decreaseMemoryPointer() {
        if (memoryPointer == 0) {
            logger.error("Fatal error of Interpreter");
            throw new InterpreterException("Index of array can not be less than zero.");
        } else {
            this.memoryPointer--;
        }
    }

    public int getMemoryPointer() {
        return this.memoryPointer;
    }

    public int getCommandPointer() {
        return this.cmdPointer;
    }

    public void setCurrentElement(byte value) {
        memory[memoryPointer] = value;
    }

    public void increaseCurrentElement() {
        if (memory[memoryPointer] == Byte.MAX_VALUE) {
            memory[memoryPointer] = 0;
        } else {
            memory[memoryPointer]++;
        }
    }

    public void decreaseCurrentElement() {
        if (memory[memoryPointer] == 0) {
            memory[memoryPointer] = Byte.MAX_VALUE;
        } else {
            memory[memoryPointer]--;
        }
    }

    public byte getElementByIndex(int index) {
        return memory[index];
    }

    public void startLoop() {
        stackCmd.push(cmdPointer + 1);
        stackMmr.push(memoryPointer);
    }

    public boolean isLoopDone() {
        return memory[stackMmr.peek()] == 0;
    }

    public void doneLoopHandler() {
        setCommandPointer(cmdPointer + 1);
        stackMmr.pop();
        stackCmd.pop();
    }

    public void repeatLoopHandler() {
        cmdPointer = getStartLoopPointer();
    }

    public int getStartLoopPointer() {
        return stackCmd.peek();
    }

}
