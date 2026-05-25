package org.example.history;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatHistory {
    private static final String HISTORY_FILE = "chat_history.txt";
    private static final String WHISPER_HISTORY_PREFIX = "whisper_";

    public static void save(String message) {
        try (FileWriter fw = new FileWriter(HISTORY_FILE, true);
             PrintWriter writer = new PrintWriter(fw)) {
            writer.println(message);
            writer.flush();
        } catch (IOException e) {
            System.err.println("Ошибка сохранения истории: " + e.getMessage());
        }
    }

    public static void saveWhisper(String nickname, String from, String to, String message) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String formatted = String.format("[%s] [%s -> %s]: %s", time, from, to, message);
        String filename = WHISPER_HISTORY_PREFIX + nickname + ".txt";

        try (FileWriter fw = new FileWriter(filename, true);
             PrintWriter writer = new PrintWriter(fw)) {
            writer.println(formatted);
            writer.flush();
        } catch (IOException e) {
            System.err.println("Ошибка сохранения личной истории: " + e.getMessage());
        }
    }

    public static void clearAllHistory() {
        File commonFile = new File(HISTORY_FILE);
        if (commonFile.exists()) {
            commonFile.delete();
            System.out.println("Общая история удалена");
        }

        File dir = new File(".");
        File[] files = dir.listFiles((d, name) -> name.startsWith(WHISPER_HISTORY_PREFIX) && name.endsWith(".txt"));

        if (files != null) {
            for (File file : files) {
                file.delete();
                System.out.println("Удалён файл: " + file.getName());
            }
        }
    }

    public static List<String> loadAll() {
        return loadFromFile(HISTORY_FILE);
    }

    public static List<String> loadWhisperHistory(String nickname) {
        String filename = WHISPER_HISTORY_PREFIX + nickname + ".txt";
        return loadFromFile(filename);
    }

    private static List<String> loadFromFile(String filename) {
        List<String> messages = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            return messages;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                messages.add(line);
            }
        } catch (IOException e) {
            System.err.println("Ошибка загрузки из файла " + filename + ": " + e.getMessage());
        }
        return messages;
    }
}