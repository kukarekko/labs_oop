package commands;

import exceptions.stack.StackUnderflowException;
import exceptions.arithmetic.DivisionByZeroException;
import stack.StackCalculator;
import java.util.logging.Logger;

public class Divide implements Operation {
    private static final Logger log = Logger.getLogger(Divide.class.getName());
    @Override
    public void apply(StackCalculator stack, String[] args) {
        if (args.length != 1) {
            log.warning("DIVIDE: invalid arguments count");
            throw new StackUnderflowException();
        }
        Double a = stack.pop();
        Double b = stack.pop();
        if (a == 0) {
            log.severe("Division by zero");
            throw new DivisionByZeroException();
        }
        stack.push(b / a);
        log.info("Result: " + b / a);
    }
}
