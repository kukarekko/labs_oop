package commands;

import stack.StackCalculator;

public interface Operation {
    void apply(StackCalculator stack, String[] args);
}
