package reader;

import exceptions.calculator.CalculatorException;
import exceptions.file.FileNotFoundException;
import java.io.File;
import java.util.Scanner;

public class Reader {
    private Scanner scanner;

    public Reader(String[] args) throws CalculatorException {
        try {
            if (args.length > 0) {
                File file = new File(args[0]);
                if (!file.exists()) {
                    throw new FileNotFoundException(args[0]);
                }
                this.scanner = new Scanner(file);
            } else {
                this.scanner = new Scanner(System.in);
            }
        } catch (java.io.FileNotFoundException e) {
            throw new FileNotFoundException(args[0]);
        }
    }

    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    public String readLine() {
        return scanner.nextLine();
    }

    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
