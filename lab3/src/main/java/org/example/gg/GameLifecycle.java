package org.example.gg;

import org.example.view.TimerPanel;

public class GameLifecycle {
    private TimerPanel timerPanel;
    private Runnable onCloseCallback;

    public GameLifecycle(TimerPanel timerPanel, Runnable onCloseCallback) {
        this.timerPanel = timerPanel;
        this.onCloseCallback = onCloseCallback;
    }

    public void startGame() {
        timerPanel.startTimer();
    }

    public void stopGame() {
        timerPanel.stopTimer();
    }

    public void onWindowClose() {
        stopGame();
        if (onCloseCallback != null) {
            onCloseCallback.run();
        }
    }

    public void returnToMenu() {
        stopGame();
        if (onCloseCallback != null) {
            onCloseCallback.run();
        }
    }
}