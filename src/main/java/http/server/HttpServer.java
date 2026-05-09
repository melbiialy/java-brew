package http.server;
import http.context.DefaultFilterChain;
import http.context.FilterChain;
import http.context.FilterContext;
import http.context.IterableFilterContext;
import http.request.HttpRequest;
import http.request.RequestReader;
import http.response.ResponseWriter;
import http.routing.Router;
import http.routing.endpoint.registry.EndPointRegistry;
import http.routing.endpoint.registry.Registry;
import http.scanner.*;
import http.utils.Banner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class HttpServer {
    private final ClassPathScanner controllerScanner;
    private final ClassPathScanner filterScanner;
    private final MethodScanner methodScanner;
    private final ExecutorService executor;
    private final Router router;
    private final ServerSocket serverSocket;
    private final RequestReader requestReader;
    private final HttpHandler httpHandler;
    private final String basePackage;
    private volatile boolean running;                  // ← volatile for thread visibility
    private final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public HttpServer(int port, String basePackage) throws IOException {
        this.basePackage = basePackage;
        this.serverSocket = new ServerSocket(port);
        this.router = new Router();
        this.requestReader = new RequestReader();
        this.running = true;

        // scan filters eagerly at construction time
        filterScanner = new FilterScanner();
        FilterChain filterChain = new DefaultFilterChain(
                new IterableFilterContext(filterScanner),
                new HttpFinalHandler(router)
        );

        this.httpHandler = new HttpHandler(filterChain, new ResponseWriter());
        this.executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() * 2  // ← bounded
        );
        this.controllerScanner = new ControllerScanner();
        this.methodScanner = new EndpointScanner();
        EndPointRegistry.getInstance();
    }

    public HttpServer(int port) throws IOException {
        this(port, "example");
    }

    public HttpServer() throws IOException {
        this(8080);
    }

    public void start() throws Exception {
        new Banner().print();
        List<Class<?>> controllers = controllerScanner.scan(basePackage);
        methodScanner.scan(controllers);
        logger.info("Server started on port: {}", serverSocket.getLocalPort());
        acceptConnections();
    }

    private void acceptConnections() throws IOException {
        while (this.running) {
            try {
                Socket socket = serverSocket.accept();
                socket.setTcpNoDelay(true);
                socket.setSoTimeout(0);
                socket.setKeepAlive(true);
                logger.info("Accepted connection from: {}", socket.getInetAddress());
                executor.execute(() -> handleConnection(socket));
            } catch (SocketException e) {
                if (!running) break; // ← expected during shutdown
                throw e;
            }
        }
    }

    private void handleConnection(Socket socket) {
        try (socket) {
            HttpRequest httpRequest;
            while ((httpRequest = requestReader.readRequest(socket)) != null) {
                boolean closeConnection = "close".equalsIgnoreCase(
                        httpRequest.getHeaders().get("Connection")
                );
                httpHandler.process(httpRequest, socket);
                if (closeConnection) break;
            }
        } catch (SocketException e) {
            logger.debug("Client disconnected: {}", e.getMessage());
        } catch (IOException e) {
            logger.warn("I/O error on connection: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error on connection", e);
        }
    }

    public void stop() throws IOException {
        this.running = false;
        serverSocket.close();        // ← unblocks accept()
        executor.shutdown();
        logger.info("Server stopped.");
    }
}