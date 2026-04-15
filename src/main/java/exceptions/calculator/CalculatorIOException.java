package exceptions.calculator;

public class CalculatorIOException extends CalculatorException {
    public CalculatorIOException(String message) {
        super(message);
    }

    public CalculatorIOException(String message, Throwable cause) {
        super(message, cause);
    }
}

