# JavaBrew HTTP Server ☕

[![progress-banner](https://backend.codecrafters.io/progress/http-server/387fa0d7-40a0-4a89-8973-3f0d54dca681)](https://app.codecrafters.io/users/codecrafters-bot?r=2qF)

A lightweight, annotation-driven HTTP/1.1 server built from scratch in Java as part of the [CodeCrafters "Build Your Own HTTP Server" Challenge](https://app.codecrafters.io/courses/http-server/overview).

> **Going Beyond the Challenge:** This implementation extends the basic requirements by introducing a Spring-like annotation-based routing framework, making it a mini web framework rather than just a simple HTTP server.

---

## ✨ Features

- **HTTP/1.1 Compliant** — Proper request/response handling following RFC 2616
- **Multi-threaded** — Handles concurrent connections via thread pool
- **Keep-Alive Support** — Persistent connections for improved performance
- **GZIP Compression** — Automatic response compression when client supports it
- **Annotation-Based Routing** — Spring-like `@Controller`, `@EndPoint`, and `@PathVariable` annotations
- **Path Variable Extraction** — Dynamic URL parameters (e.g., `/users/{id}`)
- **Automatic Type Conversion** — Path variables are cast to method parameter types
- **Classpath Scanning** — Auto-discovery of controller classes

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
│  │ • Thread    │    │ • Exception │    │ • Extract params │    │
│  │   Pool      │    │   Handling  │    │ • Invoke method  │    │
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

---

## 📁 Project Structure

```
src/main/java/
├── JavaBrew.java                    # Application entry point
└── http/
    ├── annotation/
    │   ├── Controller.java          # @Controller annotation
    │   ├── EndPoint.java            # @EndPoint annotation (path + method)
    │   └── PathVariable.java        # @PathVariable annotation
    ├── enums/
    │   ├── HttpMethod.java          # GET, POST, PUT, DELETE
    │   └── HttpStatus.java          # HTTP status codes
    ├── exception/
    │   ├── MethodNotMatchException.java   # 405 errors
    │   └── ResourceNotFoundException.java # 404 errors
    ├── request/
    │   ├── HttpRequest.java         # Request object model
    │   ├── RequestLine.java         # Method + Path + Version
    │   └── RequestReader.java       # Socket → HttpRequest parser
    ├── response/
    │   ├── HttpResponse.java        # Response object model
    │   ├── ResponseWriter.java      # HttpResponse → Socket writer
    │   └── StatusLine.java          # Version + Code + Message
    ├── routing/
    │   ├── EndpointDefinition.java  # Route metadata (record)
    │   ├── EndPointRegistry.java    # Classpath scanner
    │   ├── ParameterInfo.java       # Method parameter metadata
    │   ├── Router.java              # Request → Endpoint matcher
    │   └── UrlResolver.java         # Path matching & variable extraction
    ├── server/
    │   ├── HttpHandler.java         # Request processor
    │   └── HttpServer.java          # Main server class
    └── utils/
        └── Banner.java              # Startup banner
```

---

## 🚀 Quick Start

### Prerequisites

- Java 23+
- Maven 3.x

### Build & Run

```bash
# Build the project
mvn clean package -DskipTests

# Run the server
java -jar target/javabrew-server-1.0.0.jar

# Or use the CodeCrafters script
./your_program.sh
```

The server will start on port **8080** by default.

---

## 📝 Usage: Creating Controllers

JavaBrew uses annotations to define HTTP endpoints, similar to Spring MVC:

### Basic Controller

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

### Supported Parameter Types

Endpoint methods can receive:
- `HttpRequest` — The incoming request object
- `HttpResponse` — The response object to populate
- `@PathVariable` parameters — Automatically converted to: `String`, `int`, `long`, `double`, `float`, `boolean`, `short`, `byte`

---

## 🔧 Configuration

### Custom Port

```java
HttpServer httpServer = new HttpServer(9000); // Use port 9000
httpServer.start();
```

### Banner Configuration

Disable the startup banner via system property:
```bash
java -Djavabrew.banner.mode=off -jar javabrew-server.jar
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

Test with curl:

```bash
# Simple GET request
curl -v http://localhost:8080/

# GET with path variable
curl http://localhost:8080/api/users/42

# POST with body
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "John"}'

# Test GZIP compression
curl -H "Accept-Encoding: gzip" http://localhost:8080/api/hello --compressed

# Test keep-alive
curl -v --keepalive-time 60 http://localhost:8080/api/hello
```

---

## 🎓 What I Learned

Building this HTTP server from scratch taught me:

1. **Low-level HTTP Protocol** — Request/response structure, headers, status codes
2. **Socket Programming** — TCP connections, input/output streams, buffered reading
3. **Concurrency** — Thread pools, handling multiple simultaneous connections
4. **Java Reflection** — Runtime class inspection, method invocation, annotation processing
5. **Framework Design** — How Spring-like annotation routing works under the hood
6. **Clean Architecture** — Separation of concerns, single responsibility principle

---

## 🛣️ Roadmap

- [ ] Query parameter parsing (`?key=value`)
- [ ] Request body JSON deserialization
- [ ] Middleware/filter chain support
- [ ] Virtual threads (Project Loom)
- [ ] Static file serving
- [ ] WebSocket support

---

## 📚 Resources

- [HTTP/1.1 RFC 2616](https://www.w3.org/Protocols/rfc2616/rfc2616.html)
- [CodeCrafters HTTP Server Challenge](https://app.codecrafters.io/courses/http-server/overview)
- [Java Socket Programming](https://docs.oracle.com/javase/tutorial/networking/sockets/)

---

## 📄 License

This project was built as part of the CodeCrafters challenge for educational purposes.

---

<p align="center">
  <b>☕ JavaBrew</b> — Brewing your HTTP requests with Java excellence
</p>
