package http.exception;

/**
 * This exception is thrown to indicate that an HTTP method does not match
 * the expected or allowed method for a given operation or request.
 *
 * Typically used in scenarios where the requested method (e.g., GET, POST)
 * is not supported or does not adhere to the constraints defined by the application.
 */
public class MethodNotMatchException extends RuntimeException {
    public MethodNotMatchException(String message) {
        super(message);
    }
}

