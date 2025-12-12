package http.server;

import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServerSocket {
    private ServerSocket serverSocket;
    private int port;
    private String host;

    public HTTPServerSocket(int port) throws Exception {
        System.out.println("Starting server on port " + port);
        this.port = port;
        this.serverSocket = new ServerSocket(port);
    }
    public Socket accept() throws Exception {
        return serverSocket.accept();
    }



}
