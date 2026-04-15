package exceptions.variable;

public class UndefinedVariableException extends VariableException {
    private final String variableName;

    public UndefinedVariableException(String variableName) {
        super("Undefined variable: " + variableName);
        this.variableName = variableName;
    }
}
