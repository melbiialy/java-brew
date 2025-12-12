package http.routing;

import http.request.HTTPRequest;
import http.request.RequestLine;
import http.response.HTTPResponse;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Router {
    HashMap<String, Route> routeHashMap;

    public Router() {
        routeHashMap = new HashMap<>();
    }

    public HTTPResponse route(HTTPRequest request,HTTPResponse response) {
        List<String> pathVariable = new ArrayList<>();
        Route route = getRoute(request.getRequestLine(),pathVariable);
        if (route != null) {
            try {
                Class<?> controllerClass = route.getController();
                Object controllerInstance = controllerClass
                        .getDeclaredConstructor()
                        .newInstance();
                Method method = route.getMethod();
                method.setAccessible(true);
                method.invoke(controllerInstance, request, response,pathVariable);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else {
            response.getStatusLine().setStatusCode(404);
            response.getStatusLine().setStatusMessage("Not Found");
        }
        return response;
    }

    private Route getRoute(RequestLine requestLine, List<String> pathVariable) {
        String path = requestLine.getPath();
        String method = requestLine.getMethod();
        for (String key : routeHashMap.keySet()) {
            Route route = routeHashMap.get(key);
            if (UrlResolver.match(path, key) && route.getHttpMethod().equalsIgnoreCase(method)) {
                List<String> variables= UrlResolver.extractPathVariables(path, key);
                pathVariable.addAll(variables);
                return route;
            }
        }
        return null;
    }

    public void addEndPoint(String s, Route route) {
        routeHashMap.put(s, route);
    }
}
