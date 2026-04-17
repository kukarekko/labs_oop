package org.example.view;

import javax.swing.*;
import java.awt.*;

public class TileLabel extends JLabel {
    private int value;

    public TileLabel() {
        setHorizontalAlignment(SwingConstants.CENTER); //текст по центру
        setFont(new Font("Arial", Font.BOLD, 30));
        setOpaque(true); //видимый фон
        setPreferredSize(new Dimension(100, 100)); //рекоменд размер
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