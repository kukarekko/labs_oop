package exceptions.variable;

import exceptions.calculator.CalculatorException;

public class VariableException extends CalculatorException {
    public VariableException(String message) {
        super(message);
    }
}

