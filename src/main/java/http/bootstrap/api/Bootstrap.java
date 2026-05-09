package http.bootstrap.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import http.annotation.Controller;
import http.annotation.EndPoint;
import http.enums.ContentType;
import http.enums.HttpMethod;
import http.response.HttpResponse;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class Bootstrap {

    @EndPoint(path = "/health", method = HttpMethod.GET)
    public void health(HttpResponse response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", "UP");
        body.put("timestamp", Instant.now().toString());
        body.put("server", "java-brew/1.0");

        response.setBody(mapper.writeValueAsString(body));
        response.getHeaders().put("Content-Type", ContentType.APPLICATION_JSON.toString());
        response.getStatusLine().setStatusCode(200);
    }
    @EndPoint(path = "/ready", method = HttpMethod.GET)
    public void ready(HttpResponse response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("ready", true);
        body.put("uptime", ManagementFactory.getRuntimeMXBean().getUptime() + "ms");

        response.setBody(mapper.writeValueAsString(body));
        response.getHeaders().put("Content-Type", ContentType.APPLICATION_JSON.toString());
        response.getStatusLine().setStatusCode(200);
    }
    @EndPoint(path = "/info", method = HttpMethod.GET)
    public void info(HttpResponse response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("server", "java-brew");
        body.put("version", "1.0.0");
        body.put("java", System.getProperty("java.version"));
        body.put("os", System.getProperty("os.name"));

        response.setBody(mapper.writeValueAsString(body));
        response.getHeaders().put("Content-Type", ContentType.APPLICATION_JSON.toString());
        response.getStatusLine().setStatusCode(200);
    }
    @EndPoint(path = "/metrics", method = HttpMethod.GET)
    public void metrics(HttpResponse response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Runtime rt = Runtime.getRuntime();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("memory.used",  (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024 + " MB");
        body.put("memory.free",  rt.freeMemory()  / 1024 / 1024 + " MB");
        body.put("memory.total", rt.totalMemory() / 1024 / 1024 + " MB");
        body.put("memory.max",   rt.maxMemory()   / 1024 / 1024 + " MB");
        body.put("processors",   rt.availableProcessors());

        response.setBody(mapper.writeValueAsString(body));
        response.getHeaders().put("Content-Type", ContentType.APPLICATION_JSON.toString());
        response.getStatusLine().setStatusCode(200);
    }
}
