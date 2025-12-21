package http.server;
import http.request.HttpRequest;
import http.request.RequestReader;
import http.response.ResponseWriter;
import http.routing.Router;
import http.utils.Banner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * The HttpServer class represents a lightweight, multi-threaded HTTP server designed
 * to handle incoming HTTP requests, route them to the appropriate handlers, and send
 * responses back to clients. It supports custom endpoints and can handle multiple
 * connections using separate threads.
 */
public class HttpServer {
    private final ExecutorService executor;
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
        this.executor = Executors.newFixedThreadPool(10);
    }
    public HttpServer() throws Exception {
        this(8080);
    }
    public void start() throws Exception {
        new Banner().print();
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
            executor.execute(() -> {
                try (Socket socket1 = socket){

                    HttpRequest httpRequest ;
                    while ((httpRequest = requestReader.readRequest(socket1))!=null) {
                        boolean closeConnection = httpRequest.getHeaders().containsKey("Connection") && httpRequest.getHeaders().get("Connection").equals("close");

                        httpHandler.process(httpRequest, socket1);
                        if (closeConnection) {
                            socket1.close();
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

        }
    }

    public void stop() {
        this.running = false;
        executor.shutdown();
    }

}
