package commands;

import exceptions.stack.StackUnderflowException;
import stack.StackCalculator;
import java.util.logging.Logger;

public class Plus implements Operation {
    private static final Logger log = Logger.getLogger(Plus.class.getName());
    @Override
    public void apply(StackCalculator stack, String[] args) {
        if (args.length != 1) {
            log.warning("PLUS: invalid arguments count");
            throw new StackUnderflowException();
        }
        Double a = stack.pop();
        Double b = stack.pop();
        double result = a + b;
        stack.push(result);
        log.info("PLUS: " + a + " + " + b + " = " + result);
    }
}
