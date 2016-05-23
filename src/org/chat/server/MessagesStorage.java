package org.chat.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessagesStorage<T> {

    final private List<T> lastMessages = Collections.synchronizedList(new ArrayList<>());
    int count;

    void addToList(T message) {
        if (lastMessages.size() < count) {
            lastMessages.add(message);
        } else {
            lastMessages.remove(0);
            lastMessages.add(message);
        }
    }

    List<T> getList() {
        return lastMessages;
    }

    public void setMessagesCount(int count) {
        this.count = count;
    }
}
