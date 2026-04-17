package org.example.view;

import org.example.results.ScoreEntry;
import org.example.results.ScoreManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class FrameLeadersBoard {
    private ScoreManager scoreManager;
    private JFrame frame;
    private Runnable onCloseCallback;

    public FrameLeadersBoard(Runnable onCloseCallback) {
        this.onCloseCallback = onCloseCallback;
        initialize();
    }

    public FrameLeadersBoard() {
        this(null);
    }

    private void initialize() {
        scoreManager = new ScoreManager();

        frame = new JFrame("Leaders Board");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(243, 195, 229, 255));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(243, 195, 229, 255));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(new Color(189, 19, 144));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Arial", Font.BOLD, 16));
        exitButton.setFocusPainted(false);
        exitButton.setPreferredSize(new Dimension(70, 30));
        exitButton.setBorder(new LineBorder(Color.WHITE, 2, true));
        exitButton.addActionListener(e -> closeWindow());

        topPanel.add(exitButton, BorderLayout.EAST);
        frame.add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(243, 195, 229, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Leaderboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Consolas", Font.BOLD, 24));
        titleLabel.setForeground(new Color(189, 19, 144));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        textArea.setBackground(new Color(245, 220, 255));
        textArea.setEditable(false);
        textArea.setLineWrap(false);

        StringBuilder sb = new StringBuilder();
        List<ScoreEntry> scores = scoreManager.getScores();

        if (scores.isEmpty()) {
            sb.append(" Нет рекордов \n\n");
            sb.append("Сыграйте в игру и\n");
            sb.append("станьте первым!");
        } else {
            sb.append("  №  Имя                  Счёт   Время    Рейтинг\n");
            sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

            int rank = 1;
            for (ScoreEntry entry : scores) {
                String ratingStr = String.format("%.2f", entry.getRating());
                int minutes = entry.getSeconds() / 60;
                int secs = entry.getSeconds() % 60;
                String formattedTime = String.format("%02d:%02d", minutes, secs);

                sb.append(String.format(" %2d. %-16s %8d   %s    %s\n",
                        rank, entry.getName(), entry.getScore(), formattedTime, ratingStr));
                rank++;
            }
        }

        textArea.setText(sb.toString());

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        frame.add(mainPanel, BorderLayout.CENTER);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (onCloseCallback != null) {
                    onCloseCallback.run();
                }
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void closeWindow() {
        frame.dispose();
    }
}