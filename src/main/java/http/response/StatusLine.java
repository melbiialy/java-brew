package http.response;

/**
 * Represents the status line of an HTTP response.
 * The status line consists of the HTTP version, status code, and status message.
 * It is a required component of an HTTP response.
 */
public class StatusLine {
    private String httpVersion;
    private int statusCode;
    private String statusMessage;

    public StatusLine() {
    }
    public StatusLine(String httpVersion, int statusCode, String statusMessage) {
        this.httpVersion = httpVersion;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
