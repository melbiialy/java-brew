package http.response;

import java.util.HashMap;

public class HTTPResponse {
    private StatusLine statusLine;
    private HashMap<String, String> headers;
    private String body;
    public HTTPResponse() {
        headers = new HashMap<>();
    }
    public HTTPResponse(StatusLine statusLine, HashMap<String, String> headers, String body) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public StatusLine getStatusLine() {
        return statusLine;
    }

    public void setStatusLine(StatusLine statusLine) {
        this.statusLine = statusLine;
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
}
