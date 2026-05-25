package org.example.results;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreManager {
    private static final String SCORES_FILE = "scores.dat";
    private List<ScoreEntry> scores;

    public ScoreManager() {
        scores = loadScores();
    }

    public void addScore(String name, int score, int seconds) {
        scores.add(new ScoreEntry(name, score, seconds));
        scores.sort((a, b) -> Double.compare(b.getRating(), a.getRating()));

        if (scores.size() > 100) {
            scores = new ArrayList<>(scores.subList(0, 100));
        }
        saveScores();
    }

    public List<ScoreEntry> getScores() {
        return new ArrayList<>(scores);
    }

    public boolean isHighScores(int score, int seconds) { //проверка попадает ли в рекорды
        if (scores.size() < 100) {
            return true;
        }
        double minRating = scores.get(scores.size() - 1).getRating(); //рейтинг последнего
        double potentialRating = ScoreEntry.calculateRating(score, seconds);
        return potentialRating > minRating;
    }

    private void saveScores() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SCORES_FILE))) {
            for (ScoreEntry entry : scores) {
                writer.println(entry.getName() + "|" + entry.getScore() + "|" + entry.getSeconds() + "|" + entry.getRating());
            }
        } catch (IOException e) {
            System.err.println("Error" + e.getMessage());
            }
        }

    private List<ScoreEntry> loadScores() { //загрузка из файла
        List<ScoreEntry> loadedScores = new ArrayList<>();
        File file = new File(SCORES_FILE);

        if (!file.exists()) {
            return loadedScores;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(SCORES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    int seconds = Integer.parseInt(parts[2]);
                    loadedScores.add(new ScoreEntry(name, score, seconds));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error: " + e.getMessage());
        }

        loadedScores.sort((a, b) -> Double.compare(b.getRating(), a.getRating()));
        return loadedScores;
    }

}
