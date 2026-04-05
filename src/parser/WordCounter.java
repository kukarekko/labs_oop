package parser;

import java.util.*;

public class WordCounter {
    private Map<String, Integer> wordsMap = new HashMap<>();
    private Integer totalWords = 0;
    private ParserText parser = new ParserText();

    public void processLine(String str) {
        List<String> words = parser.parse(str);
        for (String word : words) {
            totalWords++;
            wordsMap.merge(word, 1, Integer::sum);
        }
    }

    public Map<String, Integer> getWordsMap() {
        return wordsMap;
    }

    public int getTotalWords() {
        return totalWords;
    }
}
