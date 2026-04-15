package exceptions.command;

import exceptions.calculator.CalculatorException;

public class CommandException extends CalculatorException {
    private final String commandName;
    private final String line;

    public CommandException(String message, String commandName, String line) {
        super(String.format("[Command: %s] %s (line: '%s')", commandName, message, line));
        this.commandName = commandName;
        this.line = line;
    }

    public CommandException(String message, String commandName, String line, Throwable cause) {
        super(String.format("[Command: %s] %s (line: '%s')", commandName, message, line), cause);
        this.commandName = commandName;
        this.line = line;
    }
}

