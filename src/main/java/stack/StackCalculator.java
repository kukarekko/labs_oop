package stack;

import exceptions.stack.StackUnderflowException;
import exceptions.variable.UndefinedVariableException;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class StackCalculator {
    private static final Logger log = Logger.getLogger(StackCalculator.class.getName());

    private Deque<Double> stack = new ArrayDeque<>();
    private Map<String, Double> variables = new HashMap<>();

    public void push(Double value) {
        stack.push(value);
        log.fine("Stack push: " + value + " ( " + stack.size() + ")");
    }

    public Double pop() {
        if (stack.isEmpty()) {
            log.warning("Stack pop on empty stack");
            throw new StackUnderflowException();
        }
        Double value = stack.pop();
        log.fine("Stack pop: " + value + " ( " + stack.size() + ")");
        return value;
    }

    public Double peek() {
        if (stack.isEmpty()) {
            log.warning("Stack peek on empty stack");
            throw new StackUnderflowException();
        }
        return stack.peek();
    }

    public int size(){
        return stack.size();
    }

    public void define(String name, Double value) {
        variables.put(name, value);
        log.fine("Variable defined: " + name + " = " + value);
    }

    public Double getVariable(String name) {
        Double value = variables.get(name);
        if (value == null) {
            log.warning("Undefined variable: " + name);
            throw new UndefinedVariableException(name);
        }
        log.fine("Variable retrieved: " + name + " = " + value);
        return value;
    }
    public Double resolveValue(String name) {
        try {
            return Double.parseDouble(name);
        } catch (NumberFormatException e) {
            return getVariable(name);
        }
    }
}
