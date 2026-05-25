package org.example.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

    @Test
    void testConfigHasDefaultValues() {
        assertTrue(Config.LOGGING_ENABLED);
        assertEquals(1234, Config.PORT);
        assertEquals(60000, Config.TIMEOUT_MS);
        assertNotNull(Config.PROTOCOL);
    }

    @Test
    void testProtocolIsNotEmpty() {
        assertFalse(Config.PROTOCOL.isEmpty());
    }
}