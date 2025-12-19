package http.routing;


import java.util.HashMap;

/**
 * The UrlResolver class provides utility methods for working with URL paths
 * and keys to resolve routing patterns in HTTP applications.
 * This class includes methods to match a given path and key against each other,
 * extract path variables from a URL, and normalize URL paths to create consistency.
 */
public class UrlResolver {


    public static boolean match(String path, String key) {
        if (path == null || key == null) {
            return false;
        }

        path = normalizePath(path);
        key = normalizePath(key);

        String[] pathSegments = path.split("/");
        String[] keySegments = key.split("/");

        if (pathSegments.length != keySegments.length) {
            return false;
        }

        for (int i = 0; i < keySegments.length; i++) {
            if (!isPathVariable(keySegments[i])) {
                if (!pathSegments[i].equals(keySegments[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    public static HashMap<String, String >extractPathVariables(String path, String key) {
        if (path == null || key == null) {
            return new HashMap<>();
        }

        path = normalizePath(path);
        key = normalizePath(key);

        String[] pathSegments = path.split("/");
        String[] keySegments = key.split("/");
        HashMap<String, String> pathVariables = new HashMap<>();

        if (pathSegments.length != keySegments.length) {
            return pathVariables;
        }

        for (int i = 0; i < keySegments.length; i++) {
            if (isPathVariable(keySegments[i])) {
                pathVariables.put(
                        keySegments[i].substring(1, keySegments[i].length() - 1),
                        pathSegments[i]
                );

            }
        }

        return pathVariables;
    }

    private static boolean isPathVariable(String segment) {
        return segment.startsWith("{") && segment.endsWith("}");
    }

    private static String normalizePath(String path) {

        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }
}