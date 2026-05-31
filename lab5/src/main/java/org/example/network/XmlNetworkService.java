package org.example.network;

import org.example.model.Message;
import java.io.*;
import java.net.Socket;

public class XmlNetworkService implements NetworkService {
    private Socket socket;
    private DataOutputStream dataOut;
    private DataInputStream dataIn;

    @Override
    public void connect(String host, int port, String nickname) throws IOException {
        socket = new Socket(host, port);
        dataOut = new DataOutputStream(socket.getOutputStream());
        dataIn = new DataInputStream(socket.getInputStream());
        sendMessage(new Message(nickname, "/login " + nickname));
    }

    @Override
    public void sendMessage(Message message) throws IOException {
        String xml = message.toXML();
        byte[] bytes = xml.getBytes("UTF-8");
        dataOut.writeInt(bytes.length);
        dataOut.write(bytes);
        dataOut.flush();
    }

    @Override
    public Message receiveMessage() throws IOException, ClassNotFoundException {
        int len = dataIn.readInt();
        byte[] bytes = new byte[len];
        dataIn.readFully(bytes);
        String xml = new String(bytes, "UTF-8");
        return Message.fromXML(xml);
    }

    @Override
    public void disconnect() {
        try {
            if (dataIn != null) dataIn.close();
            if (dataOut != null) dataOut.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
        }
    }
}