package http.exception;

public class UnSupportedContentType extends RuntimeException {
    public UnSupportedContentType(String message) {
        super(message);
    }
}
