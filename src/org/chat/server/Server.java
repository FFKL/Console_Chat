package org.chat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import org.chat.Message;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    private ServerSocket server;
    final private List<Connection> connections = Collections.synchronizedList(new ArrayList<>());
    final private MessagesStorage<String> messagesStorage = new MessagesStorage<>();
    final private XMLParser parser = new XMLParser();

    public Server() {
        try {
            server = new ServerSocket(parser.getPort());
            messagesStorage.setMessagesCount(parser.getMessagesLogCount());
            Integer connectionsCount = parser.getConnectionsCount();

            while (true) {
                Socket socket = server.accept();
                if (connections.size() < connectionsCount) {
                    Connection connection = new Connection(socket);
                    connections.add(connection);
                    connection.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }

    private void closeAll() {
        try {
            server.close();
            synchronized(connections) {
                for (Connection connection : connections) {
                    connection.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Connection extends Thread {
        private ObjectInputStream in;
        private ObjectOutputStream out;
        private Socket socket;

        public Connection(Socket socket) {
            this.socket = socket;
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void close() {
            try {
                in.close();
                out.close();
                socket.close();

                connections.remove(this);
            } catch (Exception e) {
                System.err.println("Потоки не были закрыты!");
            }
        }

        public void run() {
            try {
                Message message;
                List<String> lastMessages = messagesStorage.getList();
                message = (Message) in.readObject();

                synchronized (connections) {
                    for (Connection c : connections) {
                        String date = new SimpleDateFormat("dd.MM.yyyy").format(message.getDate());
                        c.out.writeObject(date + " | " + message.getName() + " подключился.");
                    }
                }
                for (String mess : lastMessages) {
                    out.writeObject(mess);
                }

                while (true) {
                    message = (Message) in.readObject();
                    if (message.getText().equals("exit")) break;
                    String date = new SimpleDateFormat("dd.MM.yyyy").format(message.getDate());
                    String messageText = date + " | " + message.getName() + ": " + message.getText();
                    synchronized (connections) {
                        for (Connection c : connections) {
                            c.out.writeObject(messageText);
                        }
                    }
                    messagesStorage.addToList(messageText);
                }
                String date = new SimpleDateFormat("dd.MM.yyyy").format(message.getDate());
                synchronized (connections) {
                    for (Connection c : connections) {
                        c.out.writeObject(date + " | " + message.getName() + " ушел.");
                    }
                }
                this.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}