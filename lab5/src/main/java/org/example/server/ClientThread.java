package org.example.server;

import org.example.history.ChatHistory;
import org.example.config.Config;
import org.example.model.Message;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientThread extends Thread {
    private ServerLogger logger;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private DataOutputStream dataOut;
    private DataInputStream dataIn;
    private String nickname;
    private boolean isLoggedIn;
    private static List<ClientThread> clients = new ArrayList<>();
    private static final Map<String, ClientThread> clientsByUuid = new HashMap<>();
    private static final Map<String, ClientThread> clientsByNickname = new HashMap<>();
    private UserSession userSession;

    public ClientThread(Socket socket) {
        this.logger = new ServerLogger();
        this.socket = socket;
        this.isLoggedIn = false;

        try {
            if (Config.PROTOCOL.equals("xml")) {
                dataOut = new DataOutputStream(socket.getOutputStream());
                dataIn = new DataInputStream(socket.getInputStream());
            } else {
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());
            }
            socket.setSoTimeout(Config.TIMEOUT_MS);
            logger.log("Потоки и таймаут настроены для нового клиента");
        } catch (IOException e) {
            logger.logError("Ошибка инициализации потоков для клиента", e);
        }
        synchronized (clients) {
            clients.add(this);
        }
        this.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                try {
                    Message message = receiveMessage();
                    handleMessage(message);
                } catch (SocketTimeoutException e) {
                    logger.log("Таймаут клиента: " + (nickname != null ? nickname : "неизвестный"));
                    break;
                } catch (EOFException e) {
                    logger.log("Клиент отключился: " + (nickname != null ? nickname : "неизвестный"));
                    break;
                }
            }
        } catch (ClassNotFoundException e) {
            logger.logError("Получен неизвестный тип сообщения", e);
        } catch (IOException e) {
            logger.logError("Ошибка ввода-вывода в потоке клиента", e);
        } finally {
            disconnect();
        }
    }

    private Message receiveMessage() throws IOException, ClassNotFoundException { //чтение сообщений
        if (Config.PROTOCOL.equals("xml")) {
            int len = dataIn.readInt();
            byte[] bytes = new byte[len];
            dataIn.readFully(bytes);
            String xml = new String(bytes, "UTF-8");
            return Message.fromXML(xml);
        } else {
            return (Message) inputStream.readObject();
        }
    }

    private void sendMessage(Message msg) throws IOException { //отправка
        if (Config.PROTOCOL.equals("xml")) {
            String xml = msg.toXML();
            byte[] bytes = xml.getBytes("UTF-8");
            dataOut.writeInt(bytes.length);
            dataOut.write(bytes);
            dataOut.flush();
        } else {
            outputStream.writeObject(msg);
            outputStream.flush();
        }
    }

    private void handleMessage(Message message) {
        String command = message.getMessage();
        String firstWord = command.split(" ")[0];

        switch (firstWord) {
            case "/login" -> handleLogin(command.substring(7).trim());
            case "/list" -> sendUserList();
            case "/users" -> sendUsersListWithUuid();
            case "/logout" -> {
                logger.log("Клиент запросил отключение: " + nickname);
                disconnect();
            }
            default -> {
                if (command.startsWith("@")) {
                    handleWhisper(command);
                } else if (isLoggedIn) {
                    logger.log("Сообщение от " + nickname + ": " + command);
                    broadcastMessage(message);
                }
            }
        }
    }

    private void handleLogin(String newNickname) {
        synchronized (clients) {
            for (ClientThread client : clients) {
                if (client != this && client.nickname != null && client.nickname.equals(newNickname)) {
                    try {
                        sendMessage(new Message("Server", "Ник занят"));
                    } catch (IOException e) {
                        logger.logError("Ошибка отправки", e);
                    }
                    logger.log("Логин: " + newNickname + " уже занят");
                    return;
                }
            }
        }
        this.userSession = new UserSession(newNickname);
        this.nickname = newNickname;
        this.isLoggedIn = true;

        synchronized (clientsByUuid) {
            clientsByUuid.put(userSession.getUuid(), this);
            clientsByNickname.put(nickname, this);
        }

        logger.log("Пользователь залогинился: " + nickname);
        try {
            sendMessage(new Message("Server", "Вы вошли как: " + nickname));
            sendChatHistory();
        } catch (IOException e) {
            logger.logError("Ошибка отправки", e);
        }
        broadcastSystemMessage(nickname + " присоединился(ась) к чату");
        broadcastUserList();
    }

    private void handleWhisper(String command) {
        String[] parts = command.split(" ", 3);
        String target;
        String messageText;
        if (parts[0].equals("/w") || parts[0].equals("/whisper")) {
            target = parts[1];
            messageText = parts.length > 2 ? parts[2] : "";
        } else if (parts[0].startsWith("@")) {
            target = parts[0].substring(1);
            messageText = parts.length > 1 ? parts[1] : "";
        } else {
            return;
        }

        if (messageText.trim().isEmpty()) {
            try {
                sendMessage(new Message("System", "Укажите текст сообщения"));
            } catch (IOException e) {}
            return;
        }

        ClientThread targetClient;
        synchronized (clientsByUuid) {
            targetClient = clientsByUuid.get(target);
            if (targetClient == null) {
                targetClient = clientsByNickname.get(target);
            }
        }

        if (targetClient == null || !targetClient.isLoggedIn) {
            try {
                sendMessage(new Message("System", "Пользователь " + target + " не найден"));
            } catch (IOException e) {}
            return;
        }

        try {
            broadcastWhisper(nickname, targetClient.nickname, messageText);
            ChatHistory.saveWhisper(nickname, nickname, targetClient.nickname, messageText);
        } catch (Exception e) {
            try {
                sendMessage(new Message("System", "Ошибка отправки личного сообщения"));
            } catch (IOException ex) {}
        }
    }

    //отправка личного сообщения
    private void broadcastWhisper(String from, String to, String text) throws IOException {
        ClientThread toClient = clientsByNickname.get(to);

        sendMessage(new Message("System", "Вы шепнули " + to + ": " + text, "whisper"));
        toClient.sendMessage(new Message("System", from + " шепнул Вам: " + text, "whisper"));
    }

    private void sendChatHistory() throws IOException {
        sendHistory(ChatHistory.loadAll(), "~~~ Общая история пуста ~~~");
        sendHistory(ChatHistory.loadWhisperHistory(nickname), "~~~ Личная история пуста ~~~");
    }

    private void sendHistory(List<String> messages, String emptyMessage) throws IOException {
        if (messages.isEmpty()) {
            sendMessage(new Message("System", emptyMessage));
        } else {
            for (String msg : messages) {
                sendMessage(new Message("System", msg));
            }
        }
    }

    private void sendUsersListWithUuid() {
        synchronized (clientsByUuid) {
            StringBuilder sb = new StringBuilder("Пользователи онлайн\n");
            for (ClientThread client : clientsByUuid.values()) {
                if (client.isLoggedIn && client.userSession != null) {
                    sb.append("  ").append(client.userSession).append("\n");
                }
            }
            try {
                sendMessage(new Message("System", sb.toString()));
            } catch (IOException e) {
                logger.logError("Ошибка отправки списка пользователей", e);
            }
        }
    }

    private void sendUserList() {
        synchronized (clients) {
            List<String> userNames = new ArrayList<>();
            for (ClientThread client : clients) {
                if (client.isLoggedIn && client.nickname != null) {
                    userNames.add(client.nickname);
                }
            }
            String[] userArray = userNames.toArray(new String[0]);
            try {
                sendMessage(new Message("Server", "/userlist", userArray));
            } catch (IOException e) {
                logger.logError("Ошибка отправки списка пользователей", e);
            }
            logger.log("Список пользователей отправлен клиенту " + nickname + ": " + userNames);
        }
    }

    private void broadcastMessage(Message message) {
        String formatted = "[" + message.getDate() + "] " + nickname + ": " + message.getMessage();
        ChatHistory.save(formatted);
        synchronized (clients) {
            for (ClientThread client : clients) {
                if (client.isLoggedIn) {
                    try {
                        client.sendMessage(new Message(nickname, formatted));
                    } catch (IOException e) {
                        logger.logError("Ошибка отправки клиенту " + client.nickname, e);
                    }
                }
            }
        }
        logger.log("Сообщение от " + nickname + " отправлено всем (" + clients.size() + " клиентов)");
    }

    private void broadcastSystemMessage(String message) {
        String formatted = "[" + new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date()) + "] " + message;
        ChatHistory.save(formatted);

        synchronized (clients) {
            for (ClientThread client : clients) {
                if (client.isLoggedIn) {
                    try {
                        client.sendMessage(new Message("System", message));
                    } catch (IOException e) {
                        logger.logError("Ошибка отправки клиенту " + client.nickname, e);
                    }
                }
            }
        }
        logger.log("Системное сообщение: " + message);
    }

    private void broadcastUserList() {
        synchronized (clients) {
            List<String> userNames = new ArrayList<>();
            for (ClientThread client : clients) {
                if (client.isLoggedIn && client.nickname != null) {
                    userNames.add(client.nickname);
                }
            }
            String[] usersArray = userNames.toArray(new String[0]);

            for (ClientThread client : clients) {
                if (client.isLoggedIn) {
                    try {
                        client.sendMessage(new Message("Server", "/userlist", usersArray));
                    } catch (IOException e) {
                        logger.logError("Ошибка отправки списка клиенту " + client.nickname, e);
                    }

                }
            }
            logger.log("Список пользователей обновлён для всех: " + userNames);
        }
    }

    private void disconnect() {
        if (isLoggedIn) {
            logger.log("Пользователь отключился: " + nickname);
            broadcastSystemMessage(nickname + " вышел из чата");

            synchronized (clientsByUuid) {
                if (userSession != null) {
                    clientsByUuid.remove(userSession.getUuid());
                }
                clientsByNickname.remove(nickname);
            }
        }

        isLoggedIn = false;

        synchronized (clients) {
            clients.remove(this);
        }
        broadcastUserList();

        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (dataIn != null) dataIn.close();
            if (dataOut != null) dataOut.close();
            if (socket != null) socket.close();
            logger.log("Соединение с клиентом закрыто: " + (nickname != null ? nickname : "неизвестный"));
        } catch (IOException e) {
            logger.logError("Ошибка закрытия соединения с клиентом", e);
        }
    }
}