package org.example.network;

import org.example.model.Message;
import java.io.IOException;

public interface NetworkService {
    void connect(String host, int port, String nickname) throws IOException;
    void sendMessage(Message message) throws IOException;
    Message receiveMessage() throws IOException, ClassNotFoundException;
    void disconnect();
}