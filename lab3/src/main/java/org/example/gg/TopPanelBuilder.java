package org.example.gg;

import org.example.view.ScorePanel;
import org.example.view.TimerPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TopPanelBuilder {

    public static JPanel createTopPanel(ScorePanel scorePanel, TimerPanel timerPanel,
                                        Runnable onExitAction) {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(243, 195, 229, 255));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel infoPanel = createInfoPanel(scorePanel, timerPanel);
        JButton exitButton = createExitButton(onExitAction);

        topPanel.add(infoPanel, BorderLayout.WEST);
        topPanel.add(exitButton, BorderLayout.EAST);

        return topPanel;
    }

    private static JPanel createInfoPanel(ScorePanel scorePanel, TimerPanel timerPanel) {
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        infoPanel.setBackground(new Color(243, 195, 229, 255));

        scorePanel.getPanel().setBackground(new Color(243, 195, 229, 255));
        timerPanel.getPanel().setBackground(new Color(243, 195, 229, 255));

        infoPanel.add(scorePanel.getPanel());
        infoPanel.add(timerPanel.getPanel());

        return infoPanel;
    }

    private static JButton createExitButton(Runnable onExitAction) {
        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(new Color(189, 19, 144));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Arial", Font.BOLD, 16));
        exitButton.setFocusPainted(false);
        exitButton.setPreferredSize(new Dimension(70, 30));
        exitButton.setBorder(new LineBorder(Color.WHITE, 2, true));
        exitButton.addActionListener(e -> onExitAction.run());

        return exitButton;
    }
}