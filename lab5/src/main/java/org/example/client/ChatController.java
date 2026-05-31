package org.example.client;

import org.example.config.Config;
import org.example.model.Message;
import org.example.network.NetworkService;
import org.example.network.SerializationNetworkService;
import org.example.network.XmlNetworkService;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatController {
    private final ClientUI ui;
    private NetworkService network;
    private String nickname;
    private volatile boolean connected = false;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    public ChatController(ClientUI ui) {
        this.ui = ui;
        ui.setController(this);
        ui.showConnectionDialog((server, port, nickname) -> {
            connect(server, port, nickname);
        });
    }

    public void connect(String host, int port, String nickname) {
        this.nickname = nickname;

        try {
            if (Config.PROTOCOL.equals("xml")) {
                network = new XmlNetworkService();
            } else if (Config.PROTOCOL.equals("serialization")){
                network = new SerializationNetworkService();
            } else {
                throw new Exception("alarm");
            }

            network.connect(host, port, nickname);
            connected = true;

            ui.setStatus(nickname + " Онлайн", new java.awt.Color(255, 255, 255));
            ui.enableInput(true);
            ui.setTitle("Чат - " + nickname);

            ui.setVisible(true);

            new Thread(this::receiveLoop).start();

        } catch (Exception e) {
            ui.setStatus("Ошибка подключения", new java.awt.Color(255, 100, 100));
            JOptionPane.showMessageDialog(ui, "Не удалось подключиться:\n" + e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            ui.showConnectionDialog(this::connect);
        }
    }

    private void receiveLoop() { //обработка вход сообщений
        try {
            while (connected) {
                Message msg = network.receiveMessage();
                processMessage(msg);
            }
        } catch (Exception e) {
            if (connected) {
                ui.appendSystemMessage("Соединение потеряно", timeFormat.format(new Date()));
                disconnect();
            }
        }
    }

    private void processMessage(Message msg) {
        String login = msg.getLogin();
        String message = msg.getMessage();
        String time = timeFormat.format(new Date());

        switch (login) {
            case "System" -> {
                if ("whisper".equals(msg.getType())) {
                    ui.appendWhisperMessage(message, time);
                } else {
                    if (message.contains("ИСТОРИЯ") || message.contains("====") || message.matches("^\\[\\d{2}:\\d{2}:\\d{2}\\].*")) {
                        ui.appendRawMessage(message + "\n");
                    } else {
                        ui.appendSystemMessage(message, time);
                    }
                }
                ui.enableInput(true);
            }
            case "Server" -> {
                switch (message) {
                    case "/userlist" -> {
                        ui.updateUserList(msg.getUsers(), nickname);
                        ui.enableInput(true);
                    }
                    case "/error" -> ui.appendSystemMessage(message, time);
                    default -> {
                        if (message.startsWith("/success")) {
                            ui.appendSystemMessage(message.substring(9), time);
                            ui.enableInput(true);
                        }
                    }
                }
            }
            default -> {
                if (message.startsWith("[")) {
                    ui.appendRawMessage(message + "\n");
                } else {
                    ui.appendMessage(login, message, time);
                }
            }
        }
    }

    void sendMessage(String text) {
        if (!connected || text.trim().isEmpty()) { return; }
        try {
            String command = text.trim();
            String firstWord = command.split(" ")[0];

            switch (firstWord) {
                case "/list" -> network.sendMessage(new Message(nickname, "/list"));
                case "/users" -> network.sendMessage(new Message(nickname, "/users"));
                case "/w", "/whisper" -> network.sendMessage(new Message(nickname, command));
                case "/logout" -> {
                    network.sendMessage(new Message(nickname, "/logout"));
                    disconnect();
                    return;
                }
                default -> {
                    if (command.startsWith("@")) {
                        network.sendMessage(new Message(nickname, command));
                    } else {
                        network.sendMessage(new Message(nickname, command));
                    }
                }
            }
            ui.clearInput();
        } catch (Exception e) {
            ui.appendSystemMessage("Ошибка отправки", timeFormat.format(new Date()));
        }
    }

    public void disconnect() {
        if (!connected) return;
        connected = false;

        if (network != null) network.disconnect();

        ui.enableInput(false);
        ui.setStatus("Отключено", new java.awt.Color(255, 100, 100));
        ui.appendSystemMessage("Вы отключились от чата", timeFormat.format(new Date()));

        int choice = JOptionPane.showConfirmDialog(ui,
                "Подключиться снова?",
                "Отключение",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            ui.dispose();

            SwingUtilities.invokeLater(() -> {
                ClientUI newUi = new ClientUI();
                new ChatController(newUi);
            });
        } else {
            System.exit(0);
        }
    }
}