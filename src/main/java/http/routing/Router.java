package http.routing;


import http.enums.HttpMethod;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class Router {
    private final HashMap<String,HashMap<HttpMethod, EndpointDefinition>> routes;
    private final EndPointRegistry registry;
    public Router() {
        routes = new HashMap<>();
        registry = new EndPointRegistry();
    }

    public void addRoute(String path, HttpMethod method, EndpointDefinition endpointDefinition) {
        HashMap<HttpMethod, EndpointDefinition> methodMap = routes.getOrDefault(path, new HashMap<>());
        methodMap.put(method, endpointDefinition);
    }
    public void registerEndPoints(String basePackage) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        registry.registerEndPoints(basePackage, this);
    }


}
