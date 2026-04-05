package commands;

import exceptions.stack.StackUnderflowException;
import exceptions.arithmetic.DivisionByZeroException;
import stack.StackCalculator;

public class Divide implements Operation {
    public Divide(String[] args) {
        if (args.length != 1) {
            throw new StackUnderflowException();
        }
    }
    @Override
    public void apply(StackCalculator stack) {
        Double a = stack.pop();
        Double b = stack.pop();
        if (a == 0) {
            throw new DivisionByZeroException();
        }
        stack.push(b / a);
    }
}
