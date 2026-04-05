package process;

import exceptions.calculator.CalculatorIOException;
import stack.StackCalculator;
import commands.Operation;
import factory.CommandFactory;
import reader.Reader;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Calculator {
    private static final Logger logger = Logger.getLogger(Calculator.class.getName());
    public void process(String[] args) {
        Reader reader = null;

        try {
            reader = new Reader(args);
            StackCalculator calculator = new StackCalculator();
            CommandFactory cmdFactory = CommandFactory.getInstance();

            while (reader.hasNextLine()) {
                String line  = reader.readLine();

                try {
                    Operation cmd = cmdFactory.createCommand(line);
                    if (cmd != null) {
                        cmd.apply(calculator);
                    }
                }
                catch (Exception e) {
                    System.err.println("Error processing line: " + line);
                    logger.log(Level.SEVERE, "Error processing line: " + line, e);
                }

            }
        } catch (CalculatorIOException e) {
            System.err.println("Error: " + e.getMessage());
            logger.log(Level.SEVERE, "IO Error", e);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            logger.log(Level.SEVERE, "Unexpected error", e);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
