package reader;

import java.io.*;
import java.util.*;

public class ReaderFile {
    public List<String> readLines(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }
}
