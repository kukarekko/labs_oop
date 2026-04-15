package commands;

import exceptions.stack.StackUnderflowException;
import stack.StackCalculator;
import java.util.logging.Logger;

public class Pop implements Operation {
    private static final Logger log = Logger.getLogger(Pop.class.getName());
    @Override
    public void apply(StackCalculator stack, String[] args) {
        if (args.length != 1) {
            log.warning("POP: invalid arguments count");
            throw new StackUnderflowException();
        }
        Double value = stack.pop();
        log.info("POP: removed " + value);
    }
}
