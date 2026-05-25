package org.example.server;

import org.junit.jupiter.api.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class UserSessionTest {

    private static final String UUID_FILE = "uuids.properties";

    @BeforeEach
    void setUp() {
        new File(UUID_FILE).delete();
    }

    @Test
    void testCreateNewUserSession() {
        UserSession session = new UserSession("Анна");

        assertEquals("Анна", session.getNickname());
        assertNotNull(session.getUuid());
        assertEquals(8, session.getUuid().length());
    }

    @Test
    void testSameUuidForSameNickname() {
        UserSession session1 = new UserSession("Анна");
        UserSession session2 = new UserSession("Анна");

        assertEquals(session1.getUuid(), session2.getUuid());
    }

    @Test
    void testDifferentUuidForDifferentNicknames() {
        UserSession session1 = new UserSession("Анна");
        UserSession session2 = new UserSession("Боб");

        assertNotEquals(session1.getUuid(), session2.getUuid());
    }

    @Test
    void testUuidIsSavedToFile() {
        UserSession session1 = new UserSession("Анна");
        String savedUuid = session1.getUuid();

        UserSession session2 = new UserSession("Анна");

        assertEquals(savedUuid, session2.getUuid());
        assertTrue(new File(UUID_FILE).exists());
    }

    @Test
    void testToString() {
        UserSession session = new UserSession("Анна");
        String str = session.toString();

        assertTrue(str.contains("Анна"));
        assertTrue(str.contains("("));
        assertTrue(str.contains(")"));
    }
}