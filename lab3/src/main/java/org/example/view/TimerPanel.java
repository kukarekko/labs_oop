package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class TimerPanel {
    private JPanel panel;
    private JLabel timerLabel;
    private Timer timer;
    private int seconds;
    private boolean isRunning;


    public TimerPanel() {
        panel = new JPanel();
        timerLabel = new JLabel("Time: 0 sec");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(timerLabel);

        seconds = 0;
        isRunning = false;

        timer = new javax.swing.Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRunning) {
                    seconds++;
                    updateTimerDisplay();
                }
            }
        });
    }

    public void startTimer() {
        seconds = 0;
        isRunning = true;
        updateTimerDisplay();
        timer.start();
    }

    public void stopTimer() {
            isRunning = false;
            if (timer != null) {
                timer.stop();
            }
    }

    public void resetTimer() {
        stopTimer();
        seconds = 0;
        updateTimerDisplay();
    }

    private void updateTimerDisplay() {
        int minutes = seconds / 60;
        int sec = seconds % 60;
        timerLabel.setText(String.format("Время: %02d:%02d", minutes, sec));
    }

    public JPanel getPanel() {
        return panel;
    }

    public int getSeconds() {
        return seconds;
    }

    public String getFormattedTime() {
        int minutes = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", minutes, sec);
    }
}
