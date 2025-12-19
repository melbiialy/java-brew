package http.exception;

/**
 * This exception is thrown to indicate that a specified resource could not be found.
 *
 * Typically used in scenarios where the application attempts to access or retrieve
 * a resource (e.g., files, database entries, or API resources) that does not exist
 * or is unavailable.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
