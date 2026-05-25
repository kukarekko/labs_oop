package org.example.launcher;

import org.example.model.Model;
import org.example.view.FrameLeadersBoard;
import org.example.view.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Launcher {
    public Launcher() {
        {
            JFrame frame = new JFrame("launcher 2048");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 500);
            frame.getContentPane().setBackground(new Color(243, 195, 229, 255));

            frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

            Buttons buttonsPanel = new Buttons();

            buttonsPanel.getPlayButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(false);

                    SizeSelectionDialog sizeDialog = new SizeSelectionDialog(frame);
                    int selectedSize = sizeDialog.showDialog();

                    if (selectedSize != -1) {
                        frame.setVisible(false);
                        Model model = new Model(selectedSize);
                        new GameFrame(model, () -> {
                            frame.setVisible(true);
                        });
                    }
                }
            });

            buttonsPanel.getLeadersBoardButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(false);
                    new FrameLeadersBoard(() -> {
                        frame.setVisible(true);
                    });
                }
            });

            buttonsPanel.getExitButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                }
            });

            frame.add(buttonsPanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }
}
