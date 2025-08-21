import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting server...");
        try (ServerSocket serverSocket = new ServerSocket(4221)) {
            serverSocket.setReuseAddress(true);

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("Accepted new connection");
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                    String line = in.readLine();
                    if (line == null || line.isEmpty()) continue;

                    String[] parts = line.split(" ");
                    String path = parts.length > 1 ? parts[1] : "/";
                    String response;

                    if ("/".equals(path)) {
                        response = "HTTP/1.1 200 OK\r\n" +
                                "Content-Length: 0\r\n" +
                                "Connection: close\r\n" +
                                "\r\n";
                    } else {
                        response = "HTTP/1.1 404 Not Found\r\n" +
                                "Content-Length: 0\r\n" +
                                "Connection: close\r\n" +
                                "\r\n";
                    }

                    out.print(response);
                    out.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
