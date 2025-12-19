package http.server;
import http.request.HttpRequest;
import http.request.RequestReader;
import http.response.ResponseWriter;
import http.routing.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



/**
 * The HttpServer class represents a lightweight, multi-threaded HTTP server designed
 * to handle incoming HTTP requests, route them to the appropriate handlers, and send
 * responses back to clients. It supports custom endpoints and can handle multiple
 * connections using separate threads.
 */
public class HttpServer {
    private final Router router;
    private final ServerSocket serverSocket;
    private final RequestReader requestReader;
    private final HttpHandler httpHandler;
    private boolean running;
    private final Logger logger = LoggerFactory.getLogger(HttpServer.class.getName());

    public HttpServer(int port) throws Exception {
        this.serverSocket = new ServerSocket(port);
        this.router = new Router();
        this.requestReader = new RequestReader();
        this.httpHandler = new HttpHandler(this.router, new ResponseWriter());
        this.running = true;
    }
    public HttpServer() throws Exception {
        this(8080);
    }
    public void start() throws Exception {
        logger.info("Starting JavaBrew server...");
        logger.info("Registering endpoints...");
        this.router.registerEndPoints("");
        logger.info("JavaBrew server started successfully! on port: {}", serverSocket.getLocalPort());
        acceptConnections();
    }

    private void acceptConnections() throws IOException {
        while (this.running){
            Socket socket = serverSocket.accept();
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(0);
            socket.setKeepAlive(true);
            logger.info("Accepted new connection from: {}", socket.getInetAddress());
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
                } catch (Exception e) {
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
