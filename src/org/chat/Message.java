package org.chat;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    String login;
    String text;
    Date date;

    Message(String login, String text, Date date) {
        this.login = login;
        this.text = text;
        this.date = date;
    }

    public String getLogin() {
        return login;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }
}
