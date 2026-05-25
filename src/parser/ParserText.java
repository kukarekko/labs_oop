package parser;

import java.util.*;

public class ParserText {
    public List<String> parse(String str) {
        if (str == null || str.isEmpty()) {
            return Collections.emptyList();
        }
        String[] words = str.split("[^\\p{L}\\p{N}]+");

        List<String> result = new ArrayList<>();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.add(word.toLowerCase());
            }
        }
        return result;
    }
}


