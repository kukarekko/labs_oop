package org.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    public static boolean LOGGING_ENABLED = true;
    public static int PORT = 1234;
    public static int TIMEOUT_MS = 60000;
    public static String PROTOCOL = "xml";

    static {
        loadConfig();
    }

    private static void loadConfig() {
        Properties properties = new Properties();

        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("prop.properties")) {

            if (input == null) {
                System.err.println("prop.properties not found in resources");
                System.err.println("Using default PORT = " + PORT);
                return;
            }

            properties.load(input);

            String portValue = properties.getProperty("PORT");

            if (portValue != null) {
                PORT = Integer.parseInt(portValue.trim());
            }

            String timeoutValue = properties.getProperty("TIMEOUT_MS");
            if (timeoutValue != null) {
                TIMEOUT_MS = Integer.parseInt(timeoutValue.trim());
            }

            String protocolValue = properties.getProperty("PROTOCOL");
            if (protocolValue != null) {
                PROTOCOL = protocolValue.trim();
            }

            System.out.println("Config loaded from resources: PORT = " + PORT);

        } catch (IOException e) {
            System.err.println("Error reading config file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid PORT value, using default: " + PORT);
        }
    }
}