package exceptions.file;

import exceptions.calculator.CalculatorException;

public class FileNotFoundException extends CalculatorException {
    public FileNotFoundException(String filename) {
        super("File not found: " + filename);
    }
}
