package commands;

import exceptions.stack.StackUnderflowException;
import stack.StackCalculator;

public class Pop implements Operation {
    @Override
    public void apply(StackCalculator stack) {
        if (stack.size() < 1) {
            throw new StackUnderflowException();
        }
        stack.pop();
    }
}
