package org.example.view;

import org.example.controller.GameController;
import org.example.model.Model;
import javax.swing.*;
import java.awt.*;

public class GameFrame {
    private JFrame frame;
    private GamePanel gamePanel;
    private TimerPanel timerPanel;
    private ScorePanel scorePanel;
    private Runnable onCloseCallback; //при закрытии окна

    public GameFrame(Model model, Runnable onCloseCallback) {
        this.onCloseCallback = onCloseCallback;

        initializeFrame();

        scorePanel = new ScorePanel();
        timerPanel = new TimerPanel();

        JPanel topPanel = createTopPanel();
        gamePanel = createGamePanel(model);

        setupKeyboard(model);
        setupWindowListener();

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(gamePanel.getPanel(), BorderLayout.CENTER);

        finishSetup();
    }

    private void initializeFrame() {
        frame = new JFrame("peekme 2048");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(450, 500);
        frame.setResizable(false); //нельзя менять размер
        frame.setLocationRelativeTo(null);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(243, 195, 229, 255));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        infoPanel.setBackground(new Color(243, 195, 229, 255));

        scorePanel.getPanel().setBackground(new Color(243, 195, 229, 255));
        timerPanel.getPanel().setBackground(new Color(243, 195, 229, 255));

        infoPanel.add(scorePanel.getPanel());
        infoPanel.add(timerPanel.getPanel());

        JButton exitButton = createExitButton();

        topPanel.add(infoPanel, BorderLayout.WEST);
        topPanel.add(exitButton, BorderLayout.EAST);

        return topPanel;
    }

    private GamePanel createGamePanel(Model model) {
        return new GamePanel(model, scorePanel, timerPanel, this);
    }

    private JButton createExitButton() {
        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(new Color(189, 19, 144));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Arial", Font.BOLD, 16));
        exitButton.setFocusPainted(false);
        exitButton.setPreferredSize(new Dimension(70, 30));
        exitButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        exitButton.addActionListener(e -> hideToMenu());
        return exitButton;
    }

    private void setupKeyboard(Model model) {
        GameController controller = new GameController(model, gamePanel);
        frame.addKeyListener(controller.getKeyListener());
    }

    private void setupWindowListener() {
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                exitApplication();
            }
        });
    }

    private void finishSetup() {
        timerPanel.startTimer();
        frame.setFocusable(true);
        frame.requestFocus();
        frame.setVisible(true);
        gamePanel.updateBoard();
    }

    public void hideToMenu() {
        if (timerPanel != null) timerPanel.stopTimer();
        frame.setVisible(false);
        frame.dispose();
        if (onCloseCallback != null) {
            onCloseCallback.run();
        }
    }

    public void closeWindow() {
        if (timerPanel != null) timerPanel.stopTimer();
        frame.dispose();
        if (onCloseCallback != null) onCloseCallback.run();
    }

    public void exitApplication() {
        if (timerPanel != null) timerPanel.stopTimer();
        frame.dispose();
        System.exit(0);
    }

    public JFrame getFrame() { return frame; }
}