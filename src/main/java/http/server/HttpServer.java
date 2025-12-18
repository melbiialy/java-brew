package http.server;
import http.request.HttpRequest;
import http.request.RequestReader;
import http.response.ResponseWriter;
import http.routing.Router;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    private final ServerSocket serverSocket;
    private final RequestReader requestReader;
    private final HttpHandler httpHandler;
    private boolean running;

    public HttpServer(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        requestReader = new RequestReader();
        Router router = new Router();
        httpHandler = new HttpHandler(router, new ResponseWriter());
        running = true;

    }
    public void start() throws Exception {
        System.out.println("Server started and listening on port " + serverSocket.getLocalPort());
        while (this.running){
            Socket socket = serverSocket.accept();
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(0);
            socket.setKeepAlive(true);
            System.out.println("accepted new connection");
            Thread thread = new Thread(()-> {
                try {

                    HttpRequest httpRequest ;
                    while ((httpRequest = requestReader.readRequest(socket))!=null) {
                        boolean closeConnection = httpRequest.getHeaders().containsKey("Connection") && httpRequest.getHeaders().get("Connection").equals("close");

                        httpHandler.process(httpRequest, socket);
                        if (closeConnection) {
                            socket.close();
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.start();
        }
    }
    public void stop() {
        this.running = false;
    }

}
