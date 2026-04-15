package commands;

import exceptions.stack.StackUnderflowException;
import stack.StackCalculator;
import java.util.logging.Logger;

public class Minus implements Operation {
    private static final Logger log = Logger.getLogger(Minus.class.getName());
    @Override
    public void apply(StackCalculator stack, String[] args) {
        if (args.length != 1) {
            log.warning("MINUS: invalid arguments count");
            throw new StackUnderflowException();
        }
        Double a = stack.pop();
        Double b = stack.pop();
        double result = b - a;
        stack.push(result);
        log.info("MINUS: " + b + " - " + a + " = " + result);
    }
}
