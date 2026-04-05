package commands;

import exceptions.stack.StackUnderflowException;
import stack.StackCalculator;

public class Multiply implements Operation {
    public Multiply(String[] args) {
        if (args.length != 1) {
            throw new StackUnderflowException();
        }
    }
    @Override
    public void apply(StackCalculator stack) {
        Double a = stack.pop();
        Double b = stack.pop();
        stack.push(a * b);
    }
}
