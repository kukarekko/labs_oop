package org.example.results;

import java.io.Serializable;

public class ScoreEntry implements Serializable{
    private String name;
    private int score;
    private int seconds;
    private double rating;

    public ScoreEntry(String name, int score, int seconds) {
        this.name = name;
        this.score = score;
        this.seconds = seconds;
        this.rating = calculateRating(score, seconds);
    }


    public static double calculateRating(int score, int seconds) {
        if (seconds == 0) return score;

        double speedBonus = 0;
        if (seconds < 60) {
            speedBonus = 0.2 * (1 - (double) seconds / 30);
        }

        // Штраф за время
        double timePenalty = (double) seconds / (score + 100);
        timePenalty = Math.min(0.5, timePenalty);

        return score * (1 - timePenalty + speedBonus);
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getSeconds() {
        return seconds;
    }

    public double getRating() {
        return rating;
    }

}
