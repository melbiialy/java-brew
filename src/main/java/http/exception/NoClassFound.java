package http.exception;

public class NoClassFound extends RuntimeException {
    public NoClassFound(String message) {
        super(message);
    }
}
