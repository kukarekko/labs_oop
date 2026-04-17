package org.example.gg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnimatedTileLabel extends JLabel {
    private int value;

    public AnimatedTileLabel() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(new Font("Arial", Font.BOLD, 30));
        setOpaque(true);
        setPreferredSize(new Dimension(100, 100));
        setValue(0);
    }

    public void setValue(int value) {
        this.value = value;
        if (value == 0) {
            setText("");
            setBackground(new Color(231, 167, 213));
        } else {
            setText(String.valueOf(value));
            setBackground(getTileColor(value));
        }
    }

    // Анимация мигания для новых плиток
    public void flash() {
        Timer timer = new Timer(50, new ActionListener() {
            int count = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (count % 2 == 0) {
                    setBackground(Color.WHITE);
                } else {
                    setBackground(getTileColor(value));
                }
                count++;
                if (count >= 6) {
                    setBackground(getTileColor(value));
                    ((Timer)e.getSource()).stop();
                }
            }
        });
        timer.start();
    }

    // Анимация для слияния
    public void mergeFlash() {
        Font originalFont = getFont();
        Timer timer = new Timer(50, new ActionListener() {
            int count = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (count == 0) {
                    setFont(new Font("Arial", Font.BOLD, 40));
                    setBackground(Color.WHITE);
                } else if (count == 3) {
                    setFont(originalFont);
                    setBackground(getTileColor(value));
                }
                count++;
                if (count >= 6) {
                    setFont(originalFont);
                    setBackground(getTileColor(value));
                    ((Timer)e.getSource()).stop();
                }
            }
        });
        timer.start();
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
}