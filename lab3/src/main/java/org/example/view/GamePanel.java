package org.example.view;

import org.example.model.Model;
import org.example.results.ScoreManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel {
    private Model model;
    private JPanel gamePanel;
    private TileLabel[][] tiles;
    private ScorePanel scorePanel;
    private TimerPanel timerPanel;
    private GameFrame gameFrame;
    private ScoreManager scoreManager;

    public GamePanel(Model model, ScorePanel scorePanel, TimerPanel timerPanel, GameFrame gameFrame) {
        this.model = model;
        this.scorePanel = scorePanel;
        this.timerPanel = timerPanel;
        this.gameFrame = gameFrame;
        this.scoreManager = new ScoreManager();

        int n = model.getN();

        gamePanel = new JPanel(new GridLayout(n, n, 5, 5));
        gamePanel.setBackground(new Color(189, 19, 144));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        tiles = new TileLabel[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = new TileLabel();
                gamePanel.add(tiles[i][j]);
            }
        }
    }

    public JPanel getPanel() {
        return gamePanel;
    }

    public void updateBoard() {
        int[][] board = model.getField();
        int score = model.getScore();
        int n = model.getN();

        scorePanel.getScoreLabel().setText("Score: " + score); //обновляем счет

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j].setValue(board[i][j]);
            }
        }

        if (model.isGameOver()) { //окончание
            timerPanel.stopTimer();

            if (scoreManager.isHighScores(score, timerPanel.getSeconds())) {

                String playerName = JOptionPane.showInputDialog(gameFrame.getFrame(),
                        "Игра окончена!\n" +
                                "Ваш счёт: " + score + "\n" +
                                "Время: " + timerPanel.getFormattedTime() + "\n\n" +
                                "Введите ваше имя:",
                        "Введите имя",
                        JOptionPane.INFORMATION_MESSAGE);

                if (playerName != null && !playerName.trim().isEmpty()) {
                    scoreManager.addScore(playerName, score, timerPanel.getSeconds());
                } else if (playerName != null) {
                    scoreManager.addScore("unknown", score, timerPanel.getSeconds());
                }
            }
            int result = JOptionPane.showConfirmDialog(gameFrame.getFrame(),
                            "Начать новую игру?",
                    "Game Over",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                newGame();
            } else {
                gameFrame.closeWindow();
            }
        }
        gameFrame.getFrame().requestFocus();
    }

    private void newGame() {
        model.reset();
        timerPanel.resetTimer();
        timerPanel.startTimer();
        updateBoard();
    }
}