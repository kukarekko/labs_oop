package org.example.server;

import org.example.config.Config;
import org.example.history.ChatHistory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
    private static ServerLogger logger;

    public static void main(String[] args) {
        ChatHistory.clearAllHistory();
        logger = new ServerLogger();
        logger.log("Запуск сервера на порту " + Config.PORT);
        try (ServerSocket socketListener = new ServerSocket(Config.PORT)) {
            logger.log("Сервер успешно запущен!");
            logger.log("Ожидание подключения клиентов...");

            while (true) {
                try {
                    Socket client = socketListener.accept();
                    logger.log("Новый клиент подключился: " + client.getInetAddress());
                    new ClientThread(client);
                } catch (IOException e) {
                    logger.logError("Ошибка при подключении клиента", e);
                }
            }
        } catch (SocketException e) {
            logger.logError("Socket исключение", e);
        } catch (IOException e) {
            logger.logError("I/O исключение", e);
        } finally {
            logger.close();
        }
    }
}