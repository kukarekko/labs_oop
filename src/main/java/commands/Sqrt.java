package commands;

import exceptions.stack.StackUnderflowException;
import exceptions.arithmetic.NegativeSqrtException;
import stack.StackCalculator;

public class Sqrt implements Operation {
    public Sqrt(String[] args) {
        if (args.length != 1) {
            throw new StackUnderflowException();
        }
    }
    @Override
    public void apply(StackCalculator stack) {
        Double a = stack.pop();
        if (a < 0) {
            throw new NegativeSqrtException(a);
        }
        stack.push(Math.sqrt(a));
    }
}
