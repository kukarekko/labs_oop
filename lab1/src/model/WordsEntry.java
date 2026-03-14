package model;

public class WordsEntry {
    private String word;
    private Integer count;
    private Double percent;

    public WordsEntry(String word, Integer count, Integer total){
        this.word = word;
        this.count = count;
        if (total == 0) {
            this.percent = 0.0;
        } else {
            this.percent = (double)count / total * 100;
        }
    }
    public int getCount() {
        return count;
    }
    public String toCsvString() {
        return String.format("%s, %d, %.2f%%", word, count, percent);
    }
}
