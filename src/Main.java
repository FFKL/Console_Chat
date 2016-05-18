import server.Server;

public class Main {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Server();
            }
        }).start();
        new Client();
    }
}
