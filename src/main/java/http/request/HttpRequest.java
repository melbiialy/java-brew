package http.request;

import java.util.HashMap;

/**
 * The HttpRequest class represents an HTTP request, encapsulating its various
 * components such as the request line, headers, and body. It provides methods
 * to construct, retrieve, and modify these components.
 *
 * Components of an HTTP request:
 * - Request Line: Contains the HTTP method, the request path, and the HTTP version.
 * - Headers: A collection of key-value pairs representing HTTP headers.
 * - Body: The optional payload or content of the HTTP request.
 */
public class HttpRequest {

    private RequestLine requestLine;
    private HashMap<String, String> headers;
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

    @Override
    public String toString() {
        return "HttpRequest{" +
                "requestLine=" + requestLine +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
