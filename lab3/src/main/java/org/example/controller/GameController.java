package org.example.controller;

import org.example.model.Model;
import org.example.view.GamePanel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameController {
    private Model model;
    private GamePanel gamePanel;

    public GameController(Model model, GamePanel gamePanel) {
        this.model = model;
        this.gamePanel = gamePanel;
    }

    public KeyListener getKeyListener() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        model.moveLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        model.moveRight();
                        break;
                    case KeyEvent.VK_UP:
                        model.moveUp();
                        break;
                    case KeyEvent.VK_DOWN:
                        model.moveDown();
                        break;
                    default:
                        return;
                }
                gamePanel.updateBoard();
            }
        };
    }
}