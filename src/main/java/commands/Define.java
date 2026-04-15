package commands;

import exceptions.stack.StackUnderflowException;
import stack.StackCalculator;
import java.util.logging.Logger;
import exceptions.variable.InvalidNumberFormatException;

public class Define implements Operation {
    private static final Logger log = Logger.getLogger(Define.class.getName());
    @Override
    public void apply(StackCalculator stack, String[] args) {
        if (args.length < 3) {
            throw new StackUnderflowException();
        }
        String name = args[1];
        try {
            double value = Double.parseDouble(args[2]);
            stack.define(name, value);
            log.info("Variable defined: " + name + " = " + value);
        } catch (NumberFormatException e) {
            log.warning("Bad number: " + args[2]);
            throw new InvalidNumberFormatException(args[2]);
        }
    }
}