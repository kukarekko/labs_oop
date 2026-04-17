package org.example.gg;

import org.example.model.Model;
import org.example.view.GameFrame;
import org.example.view.GamePanel;
import org.example.view.ScorePanel;
import org.example.view.TimerPanel;

public class GameComponents {
    private ScorePanel scorePanel;
    private TimerPanel timerPanel;
    private GamePanel gamePanel;

    public GameComponents(Model model, GameFrame gameFrame) {
        this.scorePanel = new ScorePanel();
        this.timerPanel = new TimerPanel();
        this.gamePanel = new GamePanel(model, scorePanel, timerPanel, gameFrame);
    }

    public ScorePanel getScorePanel() { return scorePanel; }
    public TimerPanel getTimerPanel() { return timerPanel; }
    public GamePanel getGamePanel() { return gamePanel; }
}