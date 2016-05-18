package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    private ServerSocket server;
    final private List<Connection> connections = Collections.synchronizedList(new ArrayList<>());
    final private MessagesStorage<String> messagesStorage = new MessagesStorage<>();

    public Server() {
        try {
            server = new ServerSocket(1234);

            while (true) {
                Socket socket = server.accept();

                Connection connection = new Connection(socket);
                connections.add(connection);
                connection.start();
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
        private BufferedReader in;
        private PrintWriter out;
        private Socket socket;

        private String name = "";

        public Connection(Socket socket) {
            this.socket = socket;

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
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
                if (connections.size() == 0) {
                    Server.this.closeAll();
                    System.exit(0);
                }
            } catch (Exception e) {
                System.err.println("Потоки не были закрыты!");
            }
        }

        public void run() {
            try {
                List<String> lastMessages = messagesStorage.getList();
                name = in.readLine();

                for (Connection c : connections) {
                    c.out.println(name + " подключился.");
                }
                for (String message : lastMessages) {
                    out.println(message);
                }

                String str;
                while (true) {
                    str = in.readLine();
                    if (str.equals("exit")) break;
                    String message = name + ": " + str;
                    for (Connection c : connections) {
                        c.out.println(message);
                    }
                    messagesStorage.addToList(message);
                }

                for (Connection c : connections) {
                    c.out.println(name + " ушел.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}