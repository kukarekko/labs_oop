package commands;

import exceptions.stack.StackUnderflowException;
import stack.StackCalculator;
import java.util.logging.Logger;

public class Push implements Operation {
    private static final Logger log = Logger.getLogger(Push.class.getName());
    @Override
    public void apply(StackCalculator stack, String[] args) {
        if (args.length < 2) {
            log.warning("Push: missing argument");
            throw new StackUnderflowException();
        }
        Double value = stack.resolveValue(args[1]);
        stack.push(value);
        log.info("PUSH: " + value);
    }
}
