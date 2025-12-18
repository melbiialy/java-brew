package http.enums;

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
