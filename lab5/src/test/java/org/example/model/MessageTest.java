package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void testCreateBroadcastMessage() {
        Message msg = new Message("Анна", "Привет всем!");

        assertEquals("Анна", msg.getLogin());
        assertEquals("Привет всем!", msg.getMessage());
        assertEquals("broadcast", msg.getType());
        assertNotNull(msg.getDate());
    }

    @Test
    void testCreateWhisperMessage() {
        Message msg = new Message("System", "Вы шепнули Анне: привет", "whisper");

        assertEquals("System", msg.getLogin());
        assertEquals("whisper", msg.getType());
    }

    @Test
    void testCreateUserListMessage() {
        String[] users = {"Анна", "Боб", "Антон"};
        Message msg = new Message("Server", "/userlist", users);

        assertEquals("Server", msg.getLogin());
        assertEquals("/userlist", msg.getMessage());
        assertEquals(3, msg.getUsers().length);
        assertEquals("Анна", msg.getUsers()[0]);
    }

    @Test
    void testToXML() {
        Message msg = new Message("Анна", "Привет & мир!");
        String xml = msg.toXML();

        assertTrue(xml.contains("<login>Анна</login>"));
        assertTrue(xml.contains("<text>Привет &amp; мир!</text>"));
        assertTrue(xml.contains("<time>"));
        assertTrue(xml.contains("</message>"));
    }

    @Test
    void testFromXML() {
        String xml = "<message><login>Анна</login><text>Привет!</text><time>1706000000000</time></message>";
        Message msg = Message.fromXML(xml);

        assertEquals("Анна", msg.getLogin());
        assertEquals("Привет!", msg.getMessage());
    }

    @Test
    void testFromXMLWithUsers() {
        String xml = "<message><login>Server</login><text>/userlist</text><users><user>Анна</user><user>Боб</user></users><time>123</time></message>";
        Message msg = Message.fromXML(xml);

        assertEquals("Server", msg.getLogin());
        assertNotNull(msg.getUsers());
        assertEquals(2, msg.getUsers().length);
        assertEquals("Анна", msg.getUsers()[0]);
        assertEquals("Боб", msg.getUsers()[1]);
    }

    @Test
    void testEscapeXml() {
        Message msg = new Message("Анна & Боб", "Текст с <тегом> и \"кавычками\"");
        String xml = msg.toXML();

        assertTrue(xml.contains("Анна &amp; Боб"));
        assertTrue(xml.contains("&lt;тегом&gt;"));
        assertTrue(xml.contains("&quot;кавычками&quot;"));
    }
}