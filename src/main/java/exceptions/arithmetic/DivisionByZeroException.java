package exceptions.arithmetic;

public class DivisionByZeroException extends ArithmeticCalculatorException {
    public DivisionByZeroException() {
        super("Division by zero");
    }
}
