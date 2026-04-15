package commands;

import exceptions.stack.StackUnderflowException;
import stack.StackCalculator;
import java.util.logging.Logger;

public class Print implements Operation {
    private static final Logger log = Logger.getLogger(Print.class.getName());
    @Override
    public void apply(StackCalculator stack, String[] args) {
        if (args.length != 1) {
            log.warning("PRINT: invalid arguments count");
            throw new StackUnderflowException();
        }
        Double value = stack.peek();
        System.out.println(value);
        log.info("PRINT: " + value);
    }
}
