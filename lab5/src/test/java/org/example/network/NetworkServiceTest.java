package org.example.network;

import org.example.model.Message;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import static org.junit.jupiter.api.Assertions.*;

class NetworkServiceTest {

    @Test
    void testXmlSerializationAndDeserialization() throws Exception {
        Message original = new Message("Анна", "Привет мир!");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        String xml = original.toXML();
        byte[] bytes = xml.getBytes("UTF-8");
        dos.writeInt(bytes.length);
        dos.write(bytes);
        dos.flush();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream dis = new DataInputStream(bais);

        int len = dis.readInt();
        byte[] receivedBytes = new byte[len];
        dis.readFully(receivedBytes);
        String receivedXml = new String(receivedBytes, "UTF-8");
        Message deserialized = Message.fromXML(receivedXml);

        assertEquals(original.getLogin(), deserialized.getLogin());
        assertEquals(original.getMessage(), deserialized.getMessage());
    }

    @Test
    void testObjectSerializationAndDeserialization() throws Exception {
        Message original = new Message("Анна", "Привет мир!");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(original);
        oos.flush();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Message deserialized = (Message) ois.readObject();

        assertEquals(original.getLogin(), deserialized.getLogin());
        assertEquals(original.getMessage(), deserialized.getMessage());
    }
}