package http.request;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private RequestLine requestLine;
    private HashMap<String, String> headers;
    private Map<String, String> pathVariables;
    private String body;
    public HttpRequest() {
        headers = new HashMap<>();
    }

    public HttpRequest(RequestLine requestLine, HashMap<String, String> headers, String body) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.body = body;
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public void setRequestLine(RequestLine requestLine) {
        this.requestLine = requestLine;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    public Map<String, String> getPathVariables() {
        return pathVariables;
    }
    public void setPathVariables(Map<String, String> pathVariables) {
        this.pathVariables = pathVariables;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "requestLine=" + requestLine +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
