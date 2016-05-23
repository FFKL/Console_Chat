package org.chat;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;

public class Message implements Serializable {

    String name;
    String text;
    Date date;

    Message(String name, String text, Date date) {
        this.name = name;
        this.text = text;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }
}
