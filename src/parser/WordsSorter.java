package parser;

import model.WordsEntry;
import java.util.*;

public class WordsSorter {

    public List<WordsEntry> sortByCount(Map<String, Integer> wordsMap, Integer totalWords) {
        List<WordsEntry> list = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : wordsMap.entrySet()) {
            list.add(new WordsEntry(entry.getKey(), entry.getValue(), totalWords));
        }

        list.sort(Comparator.comparing(WordsEntry::getCount).reversed());
        return list;
    }
}
