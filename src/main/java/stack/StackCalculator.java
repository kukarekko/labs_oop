package stack;

import exceptions.stack.StackUnderflowException;
import exceptions.variable.UndefinedVariableException;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class StackCalculator {
    private Deque<Double> stack = new ArrayDeque<>();
    private Map<String, Double> variables = new HashMap<>();

    public void push(Double value) {
        stack.push(value);
    }

    public Double pop() {
        return stack.pop();
    }

    public Double peek() {
        return stack.peek();
    }

    public int size(){
        return stack.size();
    }

    public void define(String name, Double value) {
        variables.put(name, value);
    }

    public Double getVariable(String name) {
        Double value = variables.get(name);
        if (value == null) {
            throw new UndefinedVariableException(name);
        }
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
