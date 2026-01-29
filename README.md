# JavaBrew HTTP Server ☕

[![progress-banner](https://backend.codecrafters.io/progress/http-server/387fa0d7-40a0-4a89-8973-3f0d54dca681)](https://app.codecrafters.io/users/codecrafters-bot?r=2qF)
![Java](https://img.shields.io/badge/Java-23-orange?logo=openjdk)
![Maven](https://img.shields.io/badge/Maven-3.x-blue?logo=apache-maven)
![License](https://img.shields.io/badge/License-Educational-green)

A lightweight, annotation-driven HTTP/1.1 server built from scratch in Java as part of the [CodeCrafters "Build Your Own HTTP Server" Challenge](https://app.codecrafters.io/courses/http-server/overview).

> **🚀 Going Beyond the Challenge:** This implementation extends the basic requirements by introducing a Spring-like annotation-based routing framework, making it a mini web framework rather than just a simple HTTP server.

## 📋 Table of Contents

- [Features](#-features)
- [Key Highlights](#-key-highlights)
- [Architecture](#️-architecture)
- [Quick Start](#-quick-start)
- [Usage](#-usage-creating-controllers)
- [Configuration](#-configuration)
- [HTTP Features](#-http-features-implemented)
- [Testing](#-testing)
- [Performance](#-performance)
- [What I Learned](#-what-i-learned)
- [Roadmap](#️-roadmap)
- [Resources](#-resources)

---

## ✨ Features

- **HTTP/1.1 Compliant** — Proper request/response handling following RFC 2616
- **Virtual Threads** — Modern Java 21+ virtual thread executor for high concurrency
- **Keep-Alive Support** — Persistent connections for improved performance
- **GZIP Compression** — Automatic response compression when client supports it
- **Annotation-Based Routing** — Spring-like `@Controller`, `@EndPoint`, and `@PathVariable` annotations
- **Path Variable Extraction** — Dynamic URL parameters (e.g., `/users/{id}`)
- **Automatic Type Conversion** — Path variables are cast to method parameter types (String, int, long, double, float, boolean, short, byte)
- **Classpath Scanning** — Auto-discovery of controller classes via reflection
- **Comprehensive Error Handling** — Proper HTTP status codes (404, 405, 500)
- **Structured Logging** — SLF4J + Logback integration for observability

## 🎯 Key Highlights

- **Zero Dependencies** (except logging) — Built with pure Java standard library
- **Framework-like API** — Write controllers just like Spring MVC
- **Production-Ready Architecture** — Clean separation of concerns, modular design
- **Educational Value** — Learn HTTP protocol, sockets, reflection, and concurrency

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         JavaBrew Server                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────────┐    ┌─────────────┐    ┌──────────────────┐    │
│  │ HttpServer  │───▶│ HttpHandler │───▶│     Router       │    │
│  │             │    │             │    │                  │    │
│  │ • Accept    │    │ • Process   │    │ • Match routes   │    │
│  │ • Virtual   │    │ • Exception │    │ • Extract params │    │
│  │   Threads   │    │   Handling  │    │ • Invoke method  │    │
│  │ • Keep-Alive│    │ • Headers   │    │ • Type casting   │    │
│  └─────────────┘    └─────────────┘    └──────────────────┘    │
│         │                                       │               │
│         ▼                                       ▼               │
│  ┌─────────────┐                      ┌──────────────────┐     │
│  │RequestReader│                      │ EndPointRegistry │     │
│  │             │                      │                  │     │
│  │ • Parse     │                      │ • Scan packages  │     │
│  │   request   │                      │ • Register       │     │
│  │ • Headers   │                      │   @Controller    │     │
│  │ • Body      │                      │   @EndPoint      │     │
│  └─────────────┘                      └──────────────────┘     │
│         │                                                       │
│         ▼                                                       │
│  ┌──────────────────────────────────────────────────────┐      │
│  │                  ResponseWriter                       │      │
│  │  • Status line • Headers • Body • GZIP compression   │      │
│  └──────────────────────────────────────────────────────┘      │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Request Flow

1. **Connection Accepted**: `HttpServer` accepts a new TCP connection and assigns it to a virtual thread
2. **Request Parsing**: `RequestReader` reads and parses the raw HTTP request into an `HttpRequest` object
3. **Request Processing**: `HttpHandler` processes the request, sets up response headers (GZIP, Connection)
4. **Route Matching**: `Router` matches the request path and method against registered endpoints
5. **Path Variable Extraction**: `UrlResolver` extracts path variables (e.g., `{id}` → `42`)
6. **Method Invocation**: `Router` invokes the controller method via reflection, passing `HttpRequest`, `HttpResponse`, and path variables
7. **Response Writing**: `ResponseWriter` serializes the `HttpResponse` to the socket, applying GZIP compression if needed
8. **Connection Reuse**: If keep-alive is enabled, the connection remains open for the next request

---

## 📁 Project Structure

```
src/main/java/
├── JavaBrew.java                    # Application entry point & main class
└── http/
    ├── annotation/                  # Custom annotations for routing
    │   ├── Controller.java          # @Controller annotation (class-level)
    │   ├── EndPoint.java            # @EndPoint annotation (method-level, path + method)
    │   └── PathVariable.java        # @PathVariable annotation (parameter-level)
    ├── enums/                       # HTTP-related enumerations
    │   ├── HttpMethod.java          # GET, POST, PUT, DELETE, etc.
    │   └── HttpStatus.java          # HTTP status codes (200, 404, 500, etc.)
    ├── exception/                   # Custom exceptions for HTTP errors
    │   ├── MethodNotMatchException.java   # 405 Method Not Allowed
    │   └── ResourceNotFoundException.java # 404 Not Found
    ├── request/                     # HTTP request handling
    │   ├── HttpRequest.java         # Request object model (POJO)
    │   ├── RequestLine.java         # Request line parser (Method + Path + Version)
    │   └── RequestReader.java       # Socket → HttpRequest parser
    ├── response/                    # HTTP response handling
    │   ├── HttpResponse.java        # Response object model (POJO)
    │   ├── ResponseWriter.java     # HttpResponse → Socket writer (with GZIP support)
    │   └── StatusLine.java          # Status line builder (Version + Code + Message)
    ├── routing/                     # Routing & endpoint discovery
    │   ├── EndpointDefinition.java  # Route metadata (record with controller, method, params)
    │   ├── EndPointRegistry.java    # Classpath scanner (finds @Controller classes)
    │   ├── ParameterInfo.java       # Method parameter metadata (name + type)
    │   ├── Router.java              # Request → Endpoint matcher & invoker
    │   └── UrlResolver.java         # Path matching & variable extraction
    ├── server/                      # Core server components
    │   ├── HttpHandler.java         # Request processor (orchestrates routing & response)
    │   └── HttpServer.java          # Main server class (socket management, threading)
    └── utils/
        └── Banner.java              # ASCII art startup banner
```

### Key Components Explained

- **`HttpServer`**: Manages `ServerSocket`, accepts connections, delegates to virtual thread executor
- **`HttpHandler`**: Processes requests, handles exceptions, manages response headers (GZIP, Connection)
- **`Router`**: Matches requests to endpoints, extracts path variables, invokes controller methods via reflection
- **`EndPointRegistry`**: Scans classpath for `@Controller` classes, registers routes at startup
- **`RequestReader`**: Parses raw socket input into structured `HttpRequest` object
- **`ResponseWriter`**: Writes `HttpResponse` to socket, handles GZIP compression

---

## 🚀 Quick Start

### Prerequisites

- **Java 23+** (for virtual threads support)
- **Maven 3.x** (for building)

### Installation

```bash
# Clone the repository
git clone https://github.com/yourusername/codecrafters-http-server-java.git
cd codecrafters-http-server-java

# Build the project
mvn clean package -DskipTests
```

### Running the Server

```bash
# Run the server (default port 8080)
java -jar target/javabrew-server-1.0.0.jar

# Or use the CodeCrafters script
./your_program.sh

# Run on custom port
java -cp target/javabrew-server-1.0.0.jar JavaBrew
```

The server will start on port **8080** by default and display a startup banner. You should see:
```
JavaBrew HTTP Server started successfully! on port: 8080
```

---

## 📝 Usage: Creating Controllers

JavaBrew uses annotations to define HTTP endpoints, similar to Spring MVC. Simply annotate your classes and methods, and the framework will automatically discover and register your routes.

### Basic Controller Example

```java
import http.annotation.Controller;
import http.annotation.EndPoint;
import http.annotation.PathVariable;
import http.enums.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.StatusLine;

@Controller(path = "/api")
public class UserController {

    @EndPoint(path = "/hello", method = HttpMethod.GET)
    public void hello(HttpRequest request, HttpResponse response) {
        response.setStatusLine(new StatusLine("HTTP/1.1", 200, "OK"));
        response.getHeaders().put("Content-Type", "text/plain");
        response.setBody("Hello, World!");
    }

    @EndPoint(path = "/users/{id}", method = HttpMethod.GET)
    public void getUser(
            @PathVariable("id") int userId,
            HttpRequest request,
            HttpResponse response) {
        
        response.setStatusLine(new StatusLine("HTTP/1.1", 200, "OK"));
        response.getHeaders().put("Content-Type", "application/json");
        response.setBody("{\"id\": " + userId + ", \"name\": \"John\"}");
    }

    @EndPoint(path = "/users", method = HttpMethod.POST)
    public void createUser(HttpRequest request, HttpResponse response) {
        String body = request.getBody();
        // Process the request body...
        
        response.setStatusLine(new StatusLine("HTTP/1.1", 201, "Created"));
        response.getHeaders().put("Content-Type", "application/json");
        response.setBody("{\"status\": \"created\"}");
    }
}
```

### Annotations Reference

| Annotation | Target | Description |
|------------|--------|-------------|
| `@Controller(path)` | Class | Marks a class as a controller with optional base path |
| `@EndPoint(path, method)` | Method | Maps an HTTP endpoint to a method |
| `@PathVariable(name)` | Parameter | Binds a URL path segment to a method parameter |

### Advanced Example: Multiple Path Variables

```java
@Controller(path = "/api")
public class ProductController {

    @EndPoint(path = "/products/{category}/{id}", method = HttpMethod.GET)
    public void getProduct(
            @PathVariable("category") String category,
            @PathVariable("id") int productId,
            HttpRequest request,
            HttpResponse response) {
        
        response.setStatusLine(new StatusLine("HTTP/1.1", 200, "OK"));
        response.getHeaders().put("Content-Type", "application/json");
        response.setBody(String.format(
            "{\"category\": \"%s\", \"id\": %d}", 
            category, productId
        ));
    }
}
```

### Supported Parameter Types

Endpoint methods can receive parameters in any order:
- **`HttpRequest`** — The incoming request object (headers, body, request line)
- **`HttpResponse`** — The response object to populate (status, headers, body)
- **`@PathVariable` parameters** — Automatically converted to:
  - `String`, `int`, `long`, `double`, `float`, `boolean`, `short`, `byte`
  - Both primitive and wrapper types are supported (e.g., `int` and `Integer`)

---

## 🔧 Configuration

### Custom Port

```java
// In JavaBrew.java or your main class
HttpServer httpServer = new HttpServer(9000); // Use port 9000
httpServer.start();
```

### Banner Configuration

Disable the startup banner via system property:
```bash
java -Djavabrew.banner.mode=off -jar javabrew-server.jar
```

### Logging Configuration

The server uses SLF4J with Logback. To customize logging, create a `logback.xml` in your `src/main/resources`:

```xml
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    <root level="INFO">
        <appender-ref ref="STDOUT" />
    </root>
</configuration>
```

### Graceful Shutdown

```java
HttpServer server = new HttpServer(8080);
server.start();

// Later, when shutting down:
server.stop(); // Stops accepting new connections and shuts down executor
```

---

## 📡 HTTP Features Implemented

| Feature | Status |
|---------|--------|
| GET requests | ✅ |
| POST requests | ✅ |
| PUT requests | ✅ |
| DELETE requests | ✅ |
| Request headers parsing | ✅ |
| Request body parsing | ✅ |
| Response headers | ✅ |
| Content-Length | ✅ |
| Keep-Alive connections | ✅ |
| GZIP compression | ✅ |
| Path variables | ✅ |
| 404 Not Found | ✅ |
| 405 Method Not Allowed | ✅ |
| 500 Internal Server Error | ✅ |

---

## 🧪 Testing

### Using cURL

```bash
# Simple GET request
curl -v http://localhost:8080/

# GET with path variable
curl http://localhost:8080/api/users/42

# POST with body
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "John"}'

# PUT request
curl -X PUT http://localhost:8080/api/users/42 \
  -H "Content-Type: application/json" \
  -d '{"name": "Jane"}'

# DELETE request
curl -X DELETE http://localhost:8080/api/users/42

# Test GZIP compression
curl -H "Accept-Encoding: gzip" http://localhost:8080/api/hello --compressed

# Test keep-alive (multiple requests on same connection)
curl -v --keepalive-time 60 http://localhost:8080/api/hello
```

### Using HTTPie

```bash
# GET request
http GET localhost:8080/api/users/42

# POST request
http POST localhost:8080/api/users name=John

# With headers
http GET localhost:8080/api/hello Accept-Encoding:gzip
```

### Testing Error Handling

```bash
# Test 404 Not Found
curl -v http://localhost:8080/nonexistent

# Test 405 Method Not Allowed
curl -X POST http://localhost:8080/api/users/42  # If only GET is defined
```

## ⚡ Performance

JavaBrew is designed for high concurrency:

- **Virtual Threads** — Uses Java 21+ virtual threads (`Executors.newVirtualThreadPerTaskExecutor()`), allowing millions of concurrent connections
- **Keep-Alive** — Reuses TCP connections for multiple HTTP requests, reducing overhead
- **GZIP Compression** — Reduces response size when client supports it
- **Efficient Routing** — O(n) route matching with early termination
- **Zero-Copy Where Possible** — Direct socket I/O without unnecessary buffering



## 🎓 What I Learned

Building this HTTP server from scratch taught me:

1. **Low-level HTTP Protocol** — Deep understanding of HTTP/1.1 specification (RFC 2616), request/response structure, headers, status codes, and keep-alive connections
2. **Socket Programming** — TCP socket management, `ServerSocket`, input/output streams, buffered reading, and connection lifecycle
3. **Modern Concurrency** — Virtual threads (Project Loom), thread pools, handling millions of concurrent connections efficiently
4. **Java Reflection API** — Runtime class inspection, method invocation, annotation processing, and parameter type resolution
5. **Framework Design Patterns** — How Spring-like annotation routing works under the hood, including classpath scanning and route registration
6. **Clean Architecture** — Separation of concerns, single responsibility principle, dependency injection patterns
7. **Error Handling** — Proper HTTP error responses, exception mapping, and graceful degradation
8. **Performance Optimization** — Connection reuse, compression, and efficient data structures for routing

---


## 🤝 Contributing

This is an educational project built for the CodeCrafters challenge. While contributions aren't actively sought, suggestions and improvements are welcome!

If you'd like to extend this project:
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Ideas for Contributions

- Add unit tests (JUnit)
- Implement query parameter parsing
- Add JSON body deserialization
- Improve error messages
- Add more examples

## 📚 Resources

### HTTP Protocol
- [HTTP/1.1 RFC 2616](https://www.w3.org/Protocols/rfc2616/rfc2616.html)
- [MDN HTTP Documentation](https://developer.mozilla.org/en-US/docs/Web/HTTP)

### Java & Networking
- [Java Socket Programming Tutorial](https://docs.oracle.com/javase/tutorial/networking/sockets/)
- [Java Virtual Threads (Project Loom)](https://openjdk.org/projects/loom/)
- [Java Reflection API](https://docs.oracle.com/javase/tutorial/reflect/)

### Learning Resources
- [CodeCrafters HTTP Server Challenge](https://app.codecrafters.io/courses/http-server/overview)
- [Building Web Frameworks](https://www.baeldung.com/java-web-frameworks)

---

## 📄 License

This project was built as part of the [CodeCrafters HTTP Server Challenge](https://app.codecrafters.io/courses/http-server/overview) for educational purposes.

---

## 🙏 Acknowledgments

- [CodeCrafters](https://codecrafters.io/) for the excellent learning platform
- Spring Framework team for inspiration on annotation-based routing
- Java Platform Group for virtual threads (Project Loom)

---

<p align="center">
  <b>☕ JavaBrew</b> — Brewing your HTTP requests with Java excellence<br>
  <sub>Built with ❤️ and lots of ☕</sub>
</p>
