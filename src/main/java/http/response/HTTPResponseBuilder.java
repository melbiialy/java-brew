package http.response;

public class HTTPResponseBuilder {
    private HTTPResponse response;
    public HTTPResponseBuilder() {
        response = new HTTPResponse();
    }
    public HTTPResponse build() {
        return response;
    }
    public HTTPResponseBuilder withStatusLine(StatusLine statusLine) {
        response.setStatusLine(statusLine);
        return this;
    }
    public HTTPResponseBuilder withHeader(String key, String value) {
        response.getHeaders().put(key, value);
        return this;
    }
    public HTTPResponseBuilder withBody(String body) {
        response.setBody(body);
        return this;
    }
}
