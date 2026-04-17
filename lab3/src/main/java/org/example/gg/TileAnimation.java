package org.example.gg;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TileAnimation {
    private JLabel tile;
    private int startX, startY, endX, endY;
    private int currentStep = 0;
    private int totalSteps = 10;
    private Timer timer;
    private Runnable onComplete;

    public TileAnimation(JLabel tile, int startX, int startY, int endX, int endY) {
        this.tile = tile;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public void startAnimation(Runnable onComplete) {
        this.onComplete = onComplete;
        currentStep = 0;

        timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentStep++;

                float progress = (float) currentStep / totalSteps;
                int newX = startX + (int)((endX - startX) * progress);
                int newY = startY + (int)((endY - startY) * progress);

                tile.setLocation(newX, newY);

                if (currentStep >= totalSteps) {
                    timer.stop();
                    tile.setLocation(endX, endY);
                    if (onComplete != null) {
                        onComplete.run();
                    }
                }
            }
        });
        timer.start();
    }

    public void stopAnimation() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }
}
