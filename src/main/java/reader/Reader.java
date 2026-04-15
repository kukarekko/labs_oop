package reader;

import exceptions.calculator.CalculatorException;
import exceptions.file.FileNotFoundException;
import java.io.File;
import java.util.Scanner;
import java.util.logging.Logger;

public class Reader {
    private static final Logger log = Logger.getLogger(Reader.class.getName());
    private Scanner scanner;

    public Reader(String[] args) throws CalculatorException {
        try {
            if (args.length > 0) {
                File file = new File(args[0]);
                if (!file.exists()) {
                    log.severe("File not found: " + args[0]);
                    throw new FileNotFoundException(args[0]);
                }
                this.scanner = new Scanner(file);
                log.info("Reading from file: " + args[0]);
            } else {
                this.scanner = new Scanner(System.in);
                log.info("Reading from console");
            }
        } catch (java.io.FileNotFoundException e) {
            log.severe("File not found: " + args[0]);
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
            log.fine("Reader closed");
        }
    }
}
