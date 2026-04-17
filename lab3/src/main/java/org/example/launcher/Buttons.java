package org.example.launcher;

import javax.swing.*;
import java.awt.*;

public class Buttons extends JPanel{
    private final JButton playButton;
    private final JButton exitButton;
    private final JButton leadersBoardButton;

    public Buttons() {
        setLayout(null);
        setBackground(new Color(243, 157, 217, 255));

        playButton = createButton("Play", new Color(0xD964EA), 150, 180 );
        exitButton = createButton("Exit", new Color(0xC93EDF), 150, 300);
        leadersBoardButton = createButton("Leaders Board", new Color(0xC818E4), 150, 240);

        add(playButton);
        add(exitButton);
        add(leadersBoardButton);
    }

    private JButton createButton(String name, Color color, int x, int y) {
        JButton button = new JButton(name);
        button.setBounds(x, y, 200, 50);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0xFFD1F3), 3));

        return button;
    }

    public static JButton createSizeButton(String name, int x, int y) {
        JButton button = new JButton(name);
        button.setBounds(x, y, 100, 50);
        button.setBackground(new Color(201, 62, 223));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        return button;
    }

    public JButton getPlayButton() {
        return playButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }

    public JButton getLeadersBoardButton() {
        return leadersBoardButton;
    }
}
