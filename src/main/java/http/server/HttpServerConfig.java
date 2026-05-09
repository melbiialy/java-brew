package http.server;

import http.scanner.BaseScanner;

import java.util.Objects;

public record HttpServerConfig(
        int port,
        String basePackage,
        int workerThreads,
        int shutdownTimeoutSeconds
) {

    public HttpServerConfig {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("port out of range: " + port);
        }
        if (workerThreads < 1) {
            throw new IllegalArgumentException("workerThreads must be >= 1");
        }
        if (shutdownTimeoutSeconds < 0) {
            throw new IllegalArgumentException("shutdownTimeoutSeconds must be >= 0");
        }
        Objects.requireNonNull(basePackage, "basePackage");
    }

    public static HttpServerConfig defaults() {
        return of(8080, "example");
    }

    public static HttpServerConfig of(int port, String basePackage) {
        return new HttpServerConfig(
                port,
                basePackage,
                Runtime.getRuntime().availableProcessors() * 2,
                30);
    }
}
