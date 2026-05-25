package org.example.network;

import org.example.model.Message;
import java.io.*;
import java.net.Socket;

public class SerializationNetworkService implements NetworkService {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    @Override
    public void connect(String host, int port, String nickname) throws IOException {
        socket = new Socket(host, port);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        sendMessage(new Message(nickname, "/login " + nickname));
    }

    @Override
    public void sendMessage(Message message) throws IOException {
        outputStream.writeObject(message);
        outputStream.flush();
    }

    @Override
    public Message receiveMessage() throws IOException, ClassNotFoundException {
        return (Message) inputStream.readObject();
    }

    @Override
    public void disconnect() {
        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
        }
    }
}