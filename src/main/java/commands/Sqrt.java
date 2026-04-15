package commands;

import exceptions.stack.StackUnderflowException;
import exceptions.arithmetic.NegativeSqrtException;
import stack.StackCalculator;
import java.util.logging.Logger;

public class Sqrt implements Operation {
    private static final Logger log = Logger.getLogger(Sqrt.class.getName());
    @Override
    public void apply(StackCalculator stack, String[] args) {
        if (args.length != 1) {
            log.warning("SQRT: invalid arguments count");
            throw new StackUnderflowException();
        }
        Double a = stack.pop();
        if (a < 0) {
            log.severe("SQRT: negative number - " + a);
            throw new NegativeSqrtException(a);
        }
        stack.push(Math.sqrt(a));
        log.info("Result: " + Math.sqrt(a));
    }
}
