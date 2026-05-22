package org.example.config;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    private final Properties props = new Properties();

    public Config(String filename) {
        InputStream input = getClass().getClassLoader().getResourceAsStream(filename);

        if (input == null) {
            System.err.println("ОШИБКА: Файл " + filename + " не найден!");
            return;
        }

        try {
            props.load(input);
            System.out.println("Конфиг загружен. Параметров: " + props.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key, String defaultValue) {
        String value = props.getProperty(key);
        return value != null ? value : defaultValue;
    }

    public int getInt(String key) {
        String value = props.getProperty(key);
        if (value == null) {
            System.err.println("Ключ " + key + " не найден, возвращаю 0");
            return 0;
        }
        return Integer.parseInt(value);
    }
}