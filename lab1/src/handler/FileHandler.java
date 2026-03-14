package handler;

import model.WordsEntry;
import parser.WordCounter;
import parser.WordsSorter;
import reader.ReaderFile;
import writer.CsvWriter;

import java.util.List;
import java.util.Scanner;

public class FileHandler {
    public static void fileHandler() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Введите имя файла: ");
            String fileName = scanner.nextLine();

            ReaderFile reader = new ReaderFile();
            WordCounter counter = new WordCounter();

            for (String line : reader.readLines(fileName)) {
                counter.processLine(line);
            }

            WordsSorter sorter = new WordsSorter();
            List<WordsEntry> sorted = sorter.sortByCount(counter.getWordsMap(), counter.getTotalWords());

            new CsvWriter("output.csv").write(sorted);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
