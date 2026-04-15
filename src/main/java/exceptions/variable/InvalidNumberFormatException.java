package exceptions.variable;

public class InvalidNumberFormatException extends VariableException {
    public InvalidNumberFormatException(String value) {
        super("Invalid number format: " + value);
    }
}
