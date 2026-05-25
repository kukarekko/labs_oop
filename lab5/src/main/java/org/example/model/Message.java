package org.example.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private String login;
    private String message;
    private String[] users;
    private Date time;
    private String type;

    public Message(String login, String message) { //обыч
        this(login, message, "broadcast");
    }

    public Message(String login, String message, String type) { //шепталка
        this.login = login;
        this.message = message;
        this.type = type;
        this.time = new Date();
    }

    public Message(String login, String message, String[] users) { //для списка пользователей
        this(login, message, "broadcast");
        this.users = users;
    }

    public String getType() { return type; }

    public String toXML() {
        StringBuilder xml = new StringBuilder();
        xml.append("<message>");
        xml.append("<login>").append(escapeXml(login)).append("</login>");
        xml.append("<text>").append(escapeXml(message)).append("</text>");
        if (users != null && users.length > 0) {
            xml.append("<users>");
            for (String user : users) {
                xml.append("<user>").append(escapeXml(user)).append("</user>");
            }
            xml.append("</users>");
        }
        xml.append("<time>").append(time.getTime()).append("</time>");
        xml.append("</message>");
        return xml.toString();
    }

    public static Message fromXML(String xmlString) {
        try {
            String login = extractTag(xmlString, "login");
            String text = extractTag(xmlString, "text");
            String timeStr = extractTag(xmlString, "time");

            Message msg = new Message(login, text);
            if (timeStr != null && !timeStr.isEmpty()) {
                msg.time = new Date(Long.parseLong(timeStr));
            }

            String usersBlock = extractTag(xmlString, "users");
            if (usersBlock != null && !usersBlock.isEmpty()) {
                msg.users = extractUsers(usersBlock);
            }
            return msg;
        } catch (Exception e) {
            return new Message("System", "XML parsing error");
        }
    }

    private static String extractTag(String xml, String tag) {
        String open = "<" + tag + ">";
        String close = "</" + tag + ">";
        int start = xml.indexOf(open);
        if (start == -1) return "";
        start += open.length();
        int end = xml.indexOf(close, start);
        if (end == -1) return "";
        return xml.substring(start, end);
    }

    private static String[] extractUsers(String usersBlock) {
        java.util.List<String> userList = new java.util.ArrayList<>();
        int start = 0;
        while (true) {
            int userStart = usersBlock.indexOf("<user>", start);
            if (userStart == -1) break;
            userStart += 6;
            int userEnd = usersBlock.indexOf("</user>", userStart);
            if (userEnd == -1) break;
            userList.add(usersBlock.substring(userStart, userEnd));
            start = userEnd + 7;
        }
        return userList.toArray(new String[0]);
    }

    private String escapeXml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    public String getLogin() { return login; }
    public String getMessage() { return message; }
    public String[] getUsers() { return users; }
    public String getDate() {
        return new SimpleDateFormat("HH:mm:ss").format(time);
    }
}