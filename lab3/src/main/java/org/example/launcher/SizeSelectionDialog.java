package org.example.launcher;

import javax.swing.*;
import java.awt.*;

public class SizeSelectionDialog {
    private JDialog dialog;
    private int selectedSize = -1;
    private boolean confirmed = false;

    public SizeSelectionDialog(JFrame parent) {
        dialog = new JDialog(parent, "Выберите размер поля", true);
        dialog.setSize(400, 200);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(parent);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(243, 195, 229, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Выберите размер игрового поля", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(189, 19, 144));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(new Color(243, 195, 229, 255));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton size4Button = Buttons.createSizeButton("4x4", 0, 0);
        JButton size5Button = Buttons.createSizeButton("5x5", 0, 0);
        JButton size6Button = Buttons.createSizeButton("6x6", 0, 0);

        Dimension buttonSize = new Dimension(80, 50);
        size4Button.setPreferredSize(buttonSize);
        size5Button.setPreferredSize(buttonSize);
        size6Button.setPreferredSize(buttonSize);

        size4Button.addActionListener(e -> {
            selectedSize = 4;
            confirmed = true;
            dialog.dispose();
        });

        size5Button.addActionListener(e -> {
            selectedSize = 5;
            confirmed = true;
            dialog.dispose();
        });

        size6Button.addActionListener(e -> {
            selectedSize = 6;
            confirmed = true;
            dialog.dispose();
        });

        buttonPanel.add(size4Button);
        buttonPanel.add(size5Button);
        buttonPanel.add(size6Button);

        mainPanel.add(buttonPanel);
        dialog.add(mainPanel, BorderLayout.CENTER);

    }

    public int showDialog() {
        dialog.setVisible(true);
        return confirmed ? selectedSize : -1;
    }
}