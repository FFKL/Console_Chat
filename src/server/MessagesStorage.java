package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessagesStorage<T> {

    final private List<T> lastMessages = Collections.synchronizedList(new ArrayList<>());

    void addToList(T message) {
        if (lastMessages.size() <= 10) {
            lastMessages.add(message);
        } else {
            lastMessages.remove(0);
            lastMessages.add(message);
        }
    }

    List<T> getList() {
        return lastMessages;
    }
}
