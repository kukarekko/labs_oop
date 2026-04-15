package exceptions.command;

public class CommandNotFoundException extends CommandException {
    public CommandNotFoundException(String commandName, String line) {
        super("Command not found", commandName, line);
    }
}
