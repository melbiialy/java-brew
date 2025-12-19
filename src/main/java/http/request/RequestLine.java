package http.request;

import http.enums.HttpMethod;

/**
 * Represents the request line of an HTTP request, which consists of
 * the HTTP method, the path, and the HTTP version.
 *
 * The request line is the first line of an HTTP request message and follows
 * the structure: `<HTTP_METHOD> <REQUEST_PATH> <HTTP_VERSION>`.
 */
public class RequestLine {
    private HttpMethod method;
    private String path;
    private String httpVersion;

    public RequestLine() {
    }
    public RequestLine(HttpMethod method, String path, String httpVersion) {
        this.method = method;
        this.path = path;
        this.httpVersion = httpVersion;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }
}
