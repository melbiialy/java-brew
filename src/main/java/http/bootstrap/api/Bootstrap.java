package http.bootstrap.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import http.annotation.Controller;
import http.annotation.EndPoint;
import http.enums.ContentType;
import http.enums.HttpMethod;
import http.response.HttpResponse;

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

    @EndPoint(method = HttpMethod.GET)
    public void index(HttpResponse response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "Server is running");
        body.put("endpoints", new String[]{"/health"});

        response.setBody(mapper.writeValueAsString(body));
        response.getHeaders().put("Content-Type", ContentType.APPLICATION_JSON.toString());
        response.getStatusLine().setStatusCode(200);
    }
}
