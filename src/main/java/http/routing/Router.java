package http.routing;

import exception.ResourceNotFoundException;
import http.request.HTTPRequest;

import java.lang.reflect.Method;
import java.util.HashMap;

public class Router {
    HashMap<String, Route> routeHashMap;

    public Router() {
        routeHashMap = new HashMap<>();
    }

    public Object route(HTTPRequest request) {
        Route route = routeHashMap.get(request.getRequestLine().getPath());
        if (route != null) {
            try {
                Class<?> controllerClass = route.getController();   // This is already a Class

                Object controllerInstance = controllerClass
                        .getDeclaredConstructor()
                        .newInstance();

                Method method = route.getMethod();
                method.setAccessible(true);

                return method.invoke(controllerInstance);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
       throw new ResourceNotFoundException("Not Found");
    }

    public void addEndPoint(String s, Route route) {
        routeHashMap.put(s, route);
    }
}
