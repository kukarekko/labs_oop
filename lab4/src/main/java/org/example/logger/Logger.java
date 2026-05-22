package org.example.logger;

import org.example.model.Car;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static PrintWriter writer;
    private static boolean enabled = true;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static void init(boolean enable, String logFile) {
        enabled = enable;
        if (!enabled) {
            System.out.println("Логирование отключено");
            return;
        }

        try {
            writer = new PrintWriter(new FileWriter(logFile, true));
            System.out.println("Лог-файл: " + logFile);
        } catch (IOException e) {
            System.err.println("Не удалось открыть лог-файл: " + e.getMessage());
            enabled = false;
        }
    }

    public static void log(String message) {
        if (!enabled) return;

        String timestamp = LocalDateTime.now().format(FORMATTER);
        String logMessage = timestamp + ": " + message;

        if (writer != null) {
            writer.println(logMessage);
            writer.flush();
        }
        System.out.println(logMessage);
    }

    public static void logDealerPurchase(String dealerName, Car car) {
        log(String.format("Dealer %s: %s", dealerName, car.getLogString()));
    }

    public static void logCarProduced(Car car) {
        log(String.format("Factory produced: %s", car.getLogString()));
    }

    public static void close() {
        if (writer != null) {
            writer.close();
        }
    }
}