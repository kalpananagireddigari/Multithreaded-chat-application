import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler extends Thread {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Set<PrintWriter> clientWriters;

    public ClientHandler(Socket socket, Set<PrintWriter> clientWriters) {
        this.socket = socket;
        this.clientWriters = clientWriters;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            synchronized (clientWriters) {
                clientWriters.add(out);
            }

            String message;
            while ((message = in.readLine()) != null) {
                broadcast(message);
            }

        } catch (IOException e) {
            System.out.println("Client disconnected");
        } finally {
            synchronized (clientWriters) {
                clientWriters.remove(out);
            }
            try {
                socket.close();
            } catch (IOException e) {}
        }
    }

    private void broadcast(String message) {
        synchronized (clientWriters) {
            for (PrintWriter writer : clientWriters) {
                writer.println(message);
            }
        }
    }
}
