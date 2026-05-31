package org.example.server;

import java.io.*;
import java.util.Properties;
import java.util.UUID;

public class UserSession {
    private final String nickname;
    private final String uuid;
    private static final String UUID_CONFIG_FILE = "uuids.properties";

    public UserSession(String nickname) {
        this.nickname = nickname;
        this.uuid = getOrCreateUuidForNickname(nickname);
    }

    private static String getOrCreateUuidForNickname(String nickname) {
        Properties props = new Properties();
        File file = new File(UUID_CONFIG_FILE);

        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                props.load(fis);
                String existingUuid = props.getProperty(nickname);
                if (existingUuid != null && !existingUuid.trim().isEmpty()) {
                    System.out.println("Загружен UUID для " + nickname + ": " + existingUuid);
                    return existingUuid;
                }
            } catch (IOException e) {
                System.err.println("Ошибка чтения UUID: " + e.getMessage());
            }
        }

        String newUuid = UUID.randomUUID().toString().substring(0, 8);
        props.setProperty(nickname, newUuid);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            props.store(fos, "User UUIDs - DO NOT EDIT MANUALLY");
            System.out.println("Создан новый UUID для " + nickname + ": " + newUuid);
        } catch (IOException e) {
            System.err.println("Ошибка сохранения UUID: " + e.getMessage());
        }

        return newUuid;
    }

    public String getNickname() { return nickname; }
    public String getUuid() { return uuid; }

    @Override
    public String toString() {
        return nickname + " (" + uuid + ")";
    }
}