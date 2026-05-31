package org.example.client;

import javax.swing.*;

public class Client {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            ClientUI ui = new ClientUI();
            new ChatController(ui);
        });
    }
}