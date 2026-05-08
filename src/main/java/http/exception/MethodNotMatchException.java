package http.exception;


public class MethodNotMatchException extends RuntimeException {
    public MethodNotMatchException(String message) {
        super(message);
    }
}

