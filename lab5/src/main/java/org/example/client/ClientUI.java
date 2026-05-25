package org.example.client;

import org.example.config.Config;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientUI extends JFrame {
    public JTextArea chatArea;
    public JTextField inputField;
    private JList<String> userList;
    public DefaultListModel<String> userListModel;
    public JLabel statusLabel;
    private JLabel titleLabel;
    private JButton sendButton;
    private JButton disconnectButton;
    private ChatController controller;

    public ClientUI() {
        initUI();
    }

    public void setController(ChatController controller) {
        this.controller = controller;
    }

    public void appendMessage(String sender, String message, String time) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append("[" + time + "] " + sender + ": " + message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    public void appendRawMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(message);
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    public void appendSystemMessage(String message, String time) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append("[" + time + "] " + message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    public void appendWhisperMessage(String message, String time) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append("[" + time + "] " + message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    public void updateUserList(String[] users, String currentNickname) {
        SwingUtilities.invokeLater(() -> {
            userListModel.clear();
            if (users != null) {
                for (String user : users) {
                    if (user.equals(currentNickname)) {
                        userListModel.addElement(user + " (я)");
                    } else {
                        userListModel.addElement(user);
                    }
                }
            }
        });
    }

    public void setStatus(String status, Color color) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(status);
            statusLabel.setForeground(color);
        });
    }

    public void enableInput(boolean enabled) {
        SwingUtilities.invokeLater(() -> {
            inputField.setEnabled(enabled);
            inputField.setEditable(enabled);
            sendButton.setEnabled(enabled);
            if (enabled) {
                inputField.requestFocus();
            }
        });
    }

    public void clearInput() {
        SwingUtilities.invokeLater(() -> inputField.setText(""));
    }

    public void showConnectionDialog(ConnectionCallback callback) {
        JDialog dialog = new JDialog(this, "Подключение к серверу", true);
        dialog.setSize(350, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Подключение к чату");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Сервер:"), gbc);
        JTextField serverField = new JTextField("localhost", 15);
        gbc.gridx = 1;
        panel.add(serverField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Порт:"), gbc);
        JTextField portField = new JTextField(String.valueOf(Config.PORT), 15);
        gbc.gridx = 1;
        panel.add(portField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Ваш ник:"), gbc);
        JTextField nickField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(nickField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton connectBtn = new JButton("Подключиться");
        connectBtn.setBackground(new Color(70, 130, 200));
        connectBtn.setFocusPainted(false);

        JButton cancelBtn = new JButton("Отмена");
        cancelBtn.setBackground(new Color(182, 1, 34));
        cancelBtn.addActionListener(e -> System.exit(0));

        buttonPanel.add(connectBtn);
        buttonPanel.add(cancelBtn);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        connectBtn.addActionListener(e -> {
            String server = serverField.getText().trim();
            int port;
            try {
                port = Integer.parseInt(portField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Неверный порт!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String nickname = nickField.getText().trim();
            if (nickname.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Введите ник!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            dialog.dispose();
            callback.onConnect(server, port, nickname);
        });

        dialog.setVisible(true);
    }

    private void initUI() {
        setTitle("Чат");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 248, 255));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(177, 2, 122));
        topPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        titleLabel = new JLabel("ЧАТ");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        statusLabel = new JLabel("Отключено");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(255, 200, 200));

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(statusLabel, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.7);
        splitPane.setBorder(null);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        chatArea.setBackground(Color.WHITE);
        chatArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane chatScroll = new JScrollPane(chatArea);
        chatScroll.setBorder(BorderFactory.createTitledBorder("Сообщения"));

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setFont(new Font("Arial", Font.PLAIN, 13));
        userList.setBackground(new Color(245, 245, 245));
        JScrollPane userScroll = new JScrollPane(userList);
        userScroll.setBorder(BorderFactory.createTitledBorder("Пользователи онлайн"));
        userScroll.setPreferredSize(new Dimension(180, 0));

        splitPane.setLeftComponent(chatScroll);
        splitPane.setRightComponent(userScroll);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputField.setEditable(false);

        sendButton = new JButton("Отправить");
        sendButton.setFont(new Font("Arial", Font.BOLD, 12));
        sendButton.setBackground(new Color(14, 221, 25));
        sendButton.setFocusPainted(false);
        sendButton.setEnabled(false);

        disconnectButton = new JButton("Выйти");
        disconnectButton.setFont(new Font("Arial", Font.BOLD, 12));
        disconnectButton.setBackground(new Color(211, 12, 12));
        disconnectButton.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.add(disconnectButton);

        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setupListeners();
    }

    private void sendMessageIfNeeded() {
        if (controller != null) {
            controller.sendMessage(inputField.getText());
        }
    }

    private void setupListeners() {
        inputField.addActionListener(e -> sendMessageIfNeeded());
        sendButton.addActionListener(e -> sendMessageIfNeeded());

        disconnectButton.addActionListener(e -> {
            if (controller != null) controller.disconnect();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (controller != null) controller.disconnect();
            }
        });
    }

    public interface ConnectionCallback {
        void onConnect(String server, int port, String nickname);
    }
}