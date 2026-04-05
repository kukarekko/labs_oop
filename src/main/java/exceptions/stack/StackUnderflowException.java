package exceptions.stack;

public class StackUnderflowException extends StackException {
    public StackUnderflowException() {
        super("Stack is empty");
    }
}
