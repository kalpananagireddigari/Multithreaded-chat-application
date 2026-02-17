import java.io.*;
import java.net.*;

public class ChatClient {

    public static void main(String[] args) {

        String serverAddress = "localhost";
        int port = 1234;

        try {
            Socket socket = new Socket(serverAddress, port);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader userInput = new BufferedReader(
                    new InputStreamReader(System.in));

            new Thread(() -> {
                String msg;
                try {
                    while ((msg = in.readLine()) != null) {
                        System.out.println("Message: " + msg);
                    }
                } catch (IOException e) {}
            }).start();

            String userMsg;
            while ((userMsg = userInput.readLine()) != null) {
                out.println(userMsg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
