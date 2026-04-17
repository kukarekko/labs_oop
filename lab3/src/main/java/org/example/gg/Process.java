/*
package org.example.controller;

import org.example.model.Model;
import javax.swing.*;
import java.awt.*;

public class Process {
    private Model model;
    private JFrame frame;
    private JPanel gamePanel;
    private JLabel[][] tiles;
    private JLabel scoreLabel;

    public Process(Model model) {
        this.model = model;
        int n = model.getN();

        frame = new JFrame("peekme 2048");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel scorePanel = new JPanel();
        scoreLabel = new JLabel("Счёт: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scorePanel.add(scoreLabel);
        frame.add(scorePanel, BorderLayout.NORTH);

        gamePanel = new JPanel(new GridLayout(n, n, 5, 5));
        gamePanel.setBackground(new Color(189, 19, 144));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        tiles = new JLabel[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = new JLabel("", SwingConstants.CENTER);
                tiles[i][j].setFont(new Font("Arial", Font.BOLD, 30));
                tiles[i][j].setOpaque(true);
                tiles[i][j].setBackground(new Color(133, 3, 100));
                tiles[i][j].setPreferredSize(new Dimension(100, 100));
                gamePanel.add(tiles[i][j]);
            }
        }
        frame.add(gamePanel, BorderLayout.CENTER);


        GameController keyProcess = new GameController(model, this);
        frame.addKeyListener(keyProcess.getKeyListener());


        frame.setFocusable(true);
        frame.requestFocus();
        frame.setSize(450,500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        updateBoard();
    }

    public void updateBoard() {
        int[][] board = model.getField();
        int score = model.getScore();
        int n = model.getN();

        scoreLabel.setText("Score: " + score);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int value = board[i][j];
                if (value == 0) {
                    tiles[i][j].setText("");
                    tiles[i][j].setBackground(new Color(231, 167, 213));
                } else {
                    tiles[i][j].setText(String.valueOf(value));
                    tiles[i][j].setBackground(getTileColor(value));
                }
            }
        }

        // Проверка окончания игры
        if (model.isGameOver()) {
            int result = JOptionPane.showConfirmDialog(frame,
                    "Игра окончена! Ваш счёт: " + score + "\nНачать новую игру?",
                    "Game Over",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                newGame();
            } else {
                System.exit(0);
            }
        }
        frame.requestFocus();
    }

    private Color getTileColor(int value) {
        switch (value) {
            case 2: return new Color(255, 229, 236);
            case 4: return new Color(255, 194, 209);
            case 8: return new Color(255, 179, 198);
            case 16: return new Color(255, 160, 180);
            case 32: return new Color(255, 143, 171);
            case 64: return new Color(255, 125, 150);
            case 128: return new Color(255, 110, 150);
            case 256: return new Color(255, 100, 150);
            case 512: return new Color(255, 80, 140);
            case 1024: return new Color(255, 50, 160);
            case 2048: return new Color(255, 25, 180);
            default: return new Color(255, 0, 200);
        }
    }

    private void newGame() {
        model.reset();
        updateBoard();
    }


}
*/
