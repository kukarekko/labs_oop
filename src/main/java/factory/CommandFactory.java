package factory;

import commands.Operation;
import exceptions.calculator.CalculatorIOException;
import exceptions.command.CommandInstantiationException;
import exceptions.command.CommandNotFoundException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandFactory {
    private static final Logger log = Logger.getLogger(CommandFactory.class.getName());

    private static CommandFactory instance;
    private final Map<String, Class<? extends Operation>> commands = new HashMap<>();

    private CommandFactory() {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("commands.properties")) {
            if (is == null) {
                throw new CalculatorIOException("commands.properties not found in resources!");
            }

            Properties prop = new Properties();
            prop.load(is);

            for (String key : prop.stringPropertyNames()) { //"Push"
                String className = prop.getProperty(key);   //"commands.Push"
                try {
                    Class<?> clas = Class.forName(className);

                    if (Operation.class.isAssignableFrom(clas)) {
                        commands.put(key, clas.asSubclass(Operation.class));
                        log.info("Registered command: " + key + " -> " + className);
                    } else {
                        log.warning("Class " + className + " is not an operation");
                    }
                } catch (ClassNotFoundException e) {
                    log.log(Level.SEVERE, "Class not found: " + className, e);
                }
            }
            log.info("CommandFactory initialized with " + commands.size() + " commands");

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to initialize CommandFactory", e);
            throw new CalculatorIOException("Failed to initialize CommandFactory", e);
        }
    }

    public static CommandFactory getInstance() {
        if (instance == null) {
            instance = new CommandFactory();
        }
        return instance;
    }

    public Operation createCommand(String line) throws Exception {
        if (line == null || line.trim().isEmpty()) {
            log.fine("Empty line, skipping");
            return null;
        }

        String trimmedLine = line.trim();
        if (trimmedLine.startsWith("#")) {
            log.fine("Comment line: " + trimmedLine);
            return null;
        }

        String[] parts = trimmedLine.split("\\s+");
        String commandName = parts[0].toUpperCase();

        Class<? extends Operation> clas = commands.get(commandName);
        if (clas == null) {
            log.warning("Command not found: " + commandName);
            throw new CommandNotFoundException(commandName, line);
        }

        try {
            Operation cmd = clas.getDeclaredConstructor().newInstance();
            log.info("Created command: " + commandName);
            return cmd;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to instantiate command: " + commandName, e);
            throw new CommandInstantiationException(commandName, line, e);
        }
    }
}