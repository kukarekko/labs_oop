package org.example.server;

import org.example.config.Config;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerLogger {
    private PrintWriter writer;
    private boolean enabled;

    public ServerLogger() {
        this.enabled = Config.LOGGING_ENABLED;
        if (enabled) {
            try {
                writer = new PrintWriter(new FileWriter("server.log", true));
                log(" Сервер запущен ");
            } catch (IOException e) {
                System.err.println("Не удалось создать файл лога: " + e.getMessage());
                this.enabled = false;
            }
        }
    }

    public void log(String message) {
        if (!enabled) return;

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String logMessage = time + " " + message;

        if (writer != null) {
            writer.println(logMessage);
            writer.flush();
        }
    }

    public void logError(String message, Exception e) {
        if (!enabled) return;

        log("ERROR: " + message);
        if (e != null) {
            log("  Exception: " + e.getMessage());
        }
    }

    public void close() {
        if (writer != null) {
            log(" Сервер остановлен ");
            writer.close();
        }
    }
}
