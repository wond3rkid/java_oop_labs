
package brainfuck;

import brainfuck.exception.InterpreterException;

import java.util.Arrays;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InterpreterContext {
    //scanner
    private static Logger logger = LogManager.getLogger();
    static final int ARRAY_SIZE = 65535;
    int cmdPointer;
    int memoryPointer;
    byte[] memory;
    Stack<Integer> stackCmd = new Stack<>();
    Stack<Integer> stackMmr = new Stack<>();

    InterpreterContext() {
        this.cmdPointer = 0;
        this.memoryPointer = 0;
        this.memory = new byte[ARRAY_SIZE];
        Arrays.fill(memory, (byte) 0);
    }

    public void increaseCmdPointer() {
        this.cmdPointer += 1;
    }

    public byte getCurrentElement() {
        return memory[memoryPointer];
    }

    public void setCommandPointer(int pointer) {
        this.cmdPointer = pointer;
    }

    public void setMemoryPointer(int pointer) {
        this.memoryPointer = pointer;
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
            throw new InterpreterException("Index of array can not be less than zero");
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
        //System.out.println("Current cmd pointer : " + (cmdPointer + 1));
        stackCmd.push(cmdPointer + 1);
        stackMmr.push(memoryPointer);
    }

    public boolean isLoopDone() {
        System.out.println("Loop was done well");
        return memory[stackMmr.peek()] == 0;
    }

    public void doneLoopHandler() {
        System.out.println("Now cmd pointer is :" + (cmdPointer + 1));
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
