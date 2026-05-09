# JavaBrew

a small HTTP/1.1 server i built from scratch in java. started from the codecrafters challenge, then kept going until it became a mini framework with annotations and filters, kinda like a baby spring.

no big deps, just java + slf4j + jackson.

## what it does

- speaks HTTP/1.1 (GET, POST, PUT, DELETE, headers, body, keep-alive, gzip)
- annotation routing: `@Controller`, `@EndPoint`, `@PathVariable`
- filters with a chain: `@Filter` + `BaseFilter` (like servlet filters)
- scans the classpath at startup and registers everything by itself
- path variables get cast to your method param type (int, long, String, etc.)
- proper status codes (404 / 405 / 500) handled in the bootstrap filter

## requirements

- java 23
- maven

## run it

```bash
mvn clean package -DskipTests
java -jar target/codecrafters-http-server.jar
```

default port is `8080`. default scan package is `application`.

or just:

```java
public static void main(String[] args) throws Exception {
    new HttpServer().start();
}
```

wanna change port / package?

```java
new HttpServer(9000, "com.myapp").start();
// or
new HttpServer(HttpServerConfig.of(9000, "com.myapp")).start();
```

## write a controller

drop a class in your scan package and you're done.

```java
@Controller(path = "/api")
public class UserController {

    @EndPoint(path = "/hello", method = HttpMethod.GET)
    public void hello(HttpResponse response) {
        response.setBody("hi");
        response.getStatusLine().setStatusCode(200);
    }

    @EndPoint(path = "/users/{id}", method = HttpMethod.GET)
    public void getUser(@PathVariable("id") int id, HttpResponse response) {
        response.setBody("user " + id);
    }
}
```

method params can be in any order. supported:
- `HttpRequest`, `HttpResponse`
- `@PathVariable` → `String`, `int`, `long`, `double`, `float`, `boolean`, `short`, `byte`

## write a filter

```java
@Filter
public class AuthFilter implements BaseFilter {
    @Override
    public void doFilter(HttpRequest req, HttpResponse res, FilterChain chain) {
        if (req.getHeaders().get("Authorization") == null) {
            res.getStatusLine().setStatusCode(401);
            return; // don't call chain → request stops here
        }
        chain.doChain(req, res);
    }
}
```

filters run in order before your endpoint. `BootstrapFilter` is built-in and handles gzip / connection / status mapping for you.

## project layout

```
src/main/java/
├── JavaBrew.java              # entry point
├── application/               # default scan package (your controllers go here)
└── http/
    ├── annotation/            # @Controller, @EndPoint, @PathVariable, @Filter
    ├── bootstrap/             # BootstrapFilter (gzip, conn, exception mapping)
    ├── context/               # filter chain stuff
    ├── enums/                 # HttpMethod, HttpStatus, ContentType
    ├── exception/             # 404, 405, 415 etc
    ├── request/               # parsing the request
    ├── response/              # writing the response (+ gzip)
    ├── routing/               # router, endpoints, path matching, registry
    ├── scanner/               # classpath scanners (controllers / filters / endpoints)
    ├── server/                # HttpServer, HttpHandler, HttpFinalHandler, HttpServerConfig
    └── utils/                 # banner
```

## flow

```
socket → RequestReader → HttpHandler → FilterChain → HttpFinalHandler → Router → your method
                                                                                     ↓
                                                            ResponseWriter ← HttpResponse
```

## config

`HttpServerConfig` is a record. keep it simple:

```java
new HttpServerConfig(
    8080,                              // port
    "application",                     // base package to scan
    Runtime.getRuntime().availableProcessors() * 2,  // worker threads
    30                                 // shutdown timeout in seconds
);
```

or use the factories: `HttpServerConfig.defaults()` / `HttpServerConfig.of(port, pkg)`.

## test it

```bash
curl -v http://localhost:8080/test
curl http://localhost:8080/api/users/42
curl -H "Accept-Encoding: gzip" http://localhost:8080/api/hello --compressed
```

## perf

quick `wrk` run on a simple GET (8 threads, 1000 connections, 30s):

- ~169k req/s
- ~8ms avg latency
- 0.004% error rate

depends on your machine obviously.

## what i learned

- HTTP/1.1 is simpler than people make it sound
- sockets, streams, keep-alive, all the low-level stuff
- reflection + annotations to build a routing layer
- filter chain pattern (servlet-style) is genuinely clean
- don't overengineer. one good class beats five "clean" ones


