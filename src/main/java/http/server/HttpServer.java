package http.server;

import http.context.DefaultFilterChain;
import http.context.FilterContext;
import http.context.IterableFilterContext;
import http.request.HttpRequest;
import http.request.RequestReader;
import http.response.ResponseWriter;
import http.routing.Router;
import http.routing.endpoint.registry.EndPointRegistry;
import http.routing.endpoint.registry.Registry;
import http.scanner.ControllerScanner;
import http.scanner.EndpointScanner;
import http.scanner.FilterScanner;
import http.utils.Banner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class HttpServer implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    private final HttpServerConfig config;
    private final ExecutorService executor;
    private final RequestReader requestReader = new RequestReader();

    private volatile ServerSocket serverSocket;
    private volatile boolean running;
    private final HttpHandler handler;
    private final FilterContext filterContext ;
    private final Registry endpointRegistry;

    public HttpServer() {
        this(HttpServerConfig.defaults());
    }

    public HttpServer(int port) {
        this(HttpServerConfig.of(port, "application"));
    }

    public HttpServer(int port, String basePackage) {
        this(HttpServerConfig.of(port, basePackage));
    }

    public HttpServer(HttpServerConfig config) {
        this.config = Objects.requireNonNull(config, "config");

        this.executor = Executors.newFixedThreadPool(config.workerThreads());

        filterContext = new IterableFilterContext(FilterScanner.getInstance());
        endpointRegistry = new EndPointRegistry(EndpointScanner.getInstance(),ControllerScanner.getInstance());
        Router router = new Router(endpointRegistry);
        this.handler = new HttpHandler(
                new DefaultFilterChain(
                    filterContext,
                        new HttpFinalHandler(router)),
            new ResponseWriter());
}

    public void start() throws IOException {
        if (running) {
            throw new IllegalStateException("Server is already running");
        }


        new Banner().print();
        refresh();

        this.serverSocket = new ServerSocket(config.port());
        this.running = true;
        LOGGER.info("Server started on port: {}", serverSocket.getLocalPort());

        acceptLoop();
    }

    public synchronized void refresh() {
        try {
            endpointRegistry.refresh();
            filterContext.refresh();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to register endpoints", e);
        }
        LOGGER.info("Refreshed controllers and filters from package: {}", config.basePackage());
    }

    private void acceptLoop() throws IOException {
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                socket.setTcpNoDelay(true);
                socket.setKeepAlive(true);
                LOGGER.info("Accepted connection from: {}", socket.getInetAddress());
                executor.execute(() -> handleConnection(socket));
            } catch (SocketException e) {
                if (!running) return;
                throw e;
            }
        }
    }

    private void handleConnection(Socket socket) {
        try (socket) {
            HttpRequest request;
            while ((request = requestReader.readRequest(socket)) != null) {
                boolean close = "close".equalsIgnoreCase(request.getHeaders().get("Connection"));
                handler.process(request, socket);
                if (close) break;
            }
        } catch (SocketException e) {
            LOGGER.debug("Client disconnected: {}", e.getMessage());
        } catch (IOException e) {
            LOGGER.warn("I/O error on connection: {}", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Unexpected error on connection", e);
        }
    }



    @Override
    public synchronized void close() {
        if (!running && serverSocket == null) return;
        running = false;
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            LOGGER.warn("Error closing server socket: {}", e.getMessage());
        }
        executor.shutdown();
        try {
            if (!executor.awaitTermination(config.shutdownTimeoutSeconds(), TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        LOGGER.info("Server stopped.");
    }
}
