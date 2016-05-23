package org.chat;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Scanner;

public class Client {
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;
    private Message message;
    private Resender resend = new Resender();

    public Client() {
        Scanner scan = new Scanner(System.in);

        System.out.println("Введите IP для подключения к серверу.");

        String ip = scan.nextLine();

        try {
            socket = new Socket(ip, 1234);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            String name;
            System.out.println("Введите свой ник:");
            name = scan.nextLine();
            message = new Message(name, null, new Date(System.currentTimeMillis()));
            out.writeObject(message);
            resend.start();

            String str = "";
            while (!str.equals("exit")) {
                str = scan.nextLine();
                message = new Message(name, str, new Date((System.currentTimeMillis())));
                out.writeObject(message);
            }
            resend.setStop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            System.err.println("Потоки не были закрыты!");
        }
    }

    private class Resender extends Thread {

        private boolean stoped;

        public void setStop() {
            stoped = true;
        }

        @Override
        public void run() {
            try {
                while (!stoped) {
                    String str = (String) in.readObject();
                    System.out.println(str);
                }
            } catch (IOException e) {
                System.err.println("Ошибка при получении сообщения.");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}