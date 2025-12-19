package http.enums;

/**
 * Represents HTTP status codes as an enumerated type.
 * Each status contains an associated numeric code that corresponds to a specific HTTP response status.
 */
public enum HttpStatus {
    OK(200), NOT_FOUND(404);
    final int statusCode;
     HttpStatus(int statusCode) {
        this.statusCode = statusCode;
    }
    public int getStatusCode() {
        return statusCode;
    }

}
