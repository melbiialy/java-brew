package http.routing;

import java.util.ArrayList;
import java.util.List;

public class UrlResolver {



    public static boolean match(String path, String key) {
        String[] pathSegments = path.split("/");
        String[] keySegments = key.split("/");
        if (pathSegments.length != keySegments.length) {
            return false;
        }
        for (int i = 0; i < keySegments.length; i++) {
            if (!keySegments[i].startsWith("{")&& !keySegments[i].endsWith("}")){
                if (!pathSegments[i].equals(keySegments[i])) {
                    return false;
                }
            }
        }
        return true;


    }

    public static List<String> extractPathVariables(String path, String key) {
        String[] pathSegments = path.split("/");
        String[] keySegments = key.split("/");
        List<String> pathVariables = new ArrayList<>();
        for (int i = 0; i < keySegments.length; i++) {
            if (keySegments[i].startsWith("{")&& keySegments[i].endsWith("}")){
                pathVariables.add(pathSegments[i]);

            }
        }
        System.out.println(pathVariables.size());
        return pathVariables;
    }
}
