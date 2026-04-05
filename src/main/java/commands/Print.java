package commands;

import exceptions.stack.StackUnderflowException;
import stack.StackCalculator;

public class Print implements Operation {
    public Print(String[] args) {
        if (args.length != 1) {
            throw new StackUnderflowException();
        }
    }
    @Override
    public void apply(StackCalculator stack) {
        System.out.println(stack.peek());
    }
}
