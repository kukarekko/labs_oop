package exceptions.command;

public class CommandInstantiationException extends CommandException {
    public CommandInstantiationException(String commandName, String line, Throwable cause) {
        super("Failed to instantiate command", commandName, line, cause);
    }
}
