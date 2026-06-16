package org.example;

import org.example.factory.FactoryUI;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FactoryUI());
    }
}