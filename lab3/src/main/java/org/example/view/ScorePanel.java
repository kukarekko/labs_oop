package org.example.view;

import javax.swing.*;
import java.awt.*;

public class ScorePanel {
    private JPanel panel;
    private JLabel scoreLabel;

    public ScorePanel() {
        panel = new JPanel();
        scoreLabel = new JLabel("Счёт: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(scoreLabel);
    }

    public JPanel getPanel() {
        return panel;
    }

    public JLabel getScoreLabel() {
        return scoreLabel;
    }
}