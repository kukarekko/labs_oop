package org.example.history;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChatHistoryTest {

    @BeforeEach
    void setUp() {
        // Удаляем тестовые файлы перед каждым тестом
        new File("chat_history.txt").delete();
        File[] whisperFiles = new File(".").listFiles((d, n) -> n.startsWith("whisper_") && n.endsWith(".txt"));
        if (whisperFiles != null) {
            for (File f : whisperFiles) {
                f.delete();
            }
        }
    }

    @Test
    void testSaveAndLoadCommonHistory() {
        ChatHistory.save("[12:00] Анна: Привет!");
        ChatHistory.save("[12:01] Боб: Здравствуйте!");

        List<String> history = ChatHistory.loadAll();

        assertEquals(2, history.size());
        assertEquals("[12:00] Анна: Привет!", history.get(0));
        assertEquals("[12:01] Боб: Здравствуйте!", history.get(1));
    }

    @Test
    void testSaveAndLoadWhisperHistory() {
        ChatHistory.saveWhisper("Анна", "Анна", "Боб", "Привет, как дела?");
        ChatHistory.saveWhisper("Анна", "Боб", "Анна", "Норм, спасибо!");

        List<String> history = ChatHistory.loadWhisperHistory("Анна");

        assertEquals(2, history.size());
        assertTrue(history.get(0).contains("[Анна -> Боб]"));
        assertTrue(history.get(1).contains("[Боб -> Анна]"));
    }

    @Test
    void testWhisperHistoryIsSeparateForDifferentUsers() {
        ChatHistory.saveWhisper("Анна", "Анна", "Боб", "Привет от Анны");
        ChatHistory.saveWhisper("Боб", "Боб", "Анна", "Привет от Боба");

        List<String> annaHistory = ChatHistory.loadWhisperHistory("Анна");
        List<String> bobHistory = ChatHistory.loadWhisperHistory("Боб");

        assertEquals(1, annaHistory.size());
        assertEquals(1, bobHistory.size());
        assertNotEquals(annaHistory.get(0), bobHistory.get(0));
    }

    @Test
    void testLoadEmptyHistory() {
        List<String> history = ChatHistory.loadAll();
        assertTrue(history.isEmpty());
    }

    @Test
    void testLoadEmptyWhisperHistory() {
        List<String> history = ChatHistory.loadWhisperHistory("Несуществующий");
        assertTrue(history.isEmpty());
    }

    @Test
    void testClearAllHistory() {
        ChatHistory.save("Сообщение 1");
        ChatHistory.save("Сообщение 2");
        ChatHistory.saveWhisper("Анна", "Анна", "Боб", "Секрет");

        ChatHistory.clearAllHistory();

        assertTrue(ChatHistory.loadAll().isEmpty());
        assertTrue(ChatHistory.loadWhisperHistory("Анна").isEmpty());
    }
}