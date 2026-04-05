package commands;

import exceptions.stack.StackUnderflowException;
import stack.StackCalculator;

public class Push implements Operation {
    private String arg;
    public Push(String[] args) {
        if (args.length < 2) {
            throw new StackUnderflowException();
        }
        this.arg = args[1];
    }
    @Override
    public void apply(StackCalculator stack) {
        Double value = stack.resolveValue(arg);
        stack.push(value);
    }
}
