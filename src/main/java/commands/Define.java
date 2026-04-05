package commands;

import exceptions.stack.StackUnderflowException;
import stack.StackCalculator;
import exceptions.variable.InvalidNumberFormatException;

public class Define implements Operation {
    private String name;
    private Double value;

    public Define(String[] args) {
        if (args.length < 3) {
            throw new StackUnderflowException();
        }
        this.name = args[1];
        try {
            this.value = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            throw new InvalidNumberFormatException(args[2]);
        }
    }

    @Override
    public void apply(StackCalculator stack) {
        stack.define(name, value);
    }
}