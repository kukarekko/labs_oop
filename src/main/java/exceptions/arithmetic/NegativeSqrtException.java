package exceptions.arithmetic;

public class NegativeSqrtException extends ArithmeticCalculatorException {
    public NegativeSqrtException(double value) {
        super("Cannot calculate square root of negative number: " + value);
    }
}
