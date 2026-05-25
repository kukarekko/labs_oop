package writer;

import model.WordsEntry;
import java.io.*;
import java.util.List;

public class CsvWriter {
    private final String fileName;

    public CsvWriter(String fileName) {
        this.fileName = fileName;
    }

    public void write(List<WordsEntry> entries) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (WordsEntry entry : entries) {
                writer.write(entry.toCsvString() + "\n");
            }
        }
    }
}
