package exceptions.stack;

import exceptions.calculator.CalculatorException;

public class StackException extends CalculatorException {
    public StackException(String message) {
        super(message);
    }
}
