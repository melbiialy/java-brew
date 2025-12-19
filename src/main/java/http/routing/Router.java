package http.routing;


import http.enums.HttpMethod;
import http.exception.MethodNotMatchException;
import http.exception.ResourceNotFoundException;
import http.request.HttpRequest;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * A Router class responsible for mapping HTTP requests to their respective
 * endpoint definitions and executing the appropriate logic.
 * It supports registering routes dynamically and routing HTTP requests
 * based on the provided path and method.
 */
public class Router {
    private static final Logger log = LoggerFactory.getLogger(Router.class);
    private final HashMap<String,HashMap<HttpMethod, EndpointDefinition>> routes;
    private final EndPointRegistry registry;

    public Router() {
        routes = new HashMap<>();
        registry = new EndPointRegistry();
    }

    public void addRoute(String path, HttpMethod method, EndpointDefinition endpointDefinition) {
        routes.computeIfAbsent(path, k -> new HashMap<>())
                .put(method, endpointDefinition);
    }
    public void registerEndPoints(String basePackage) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        registry.registerEndPoints(basePackage, this);
    }


    public void route(HttpRequest httpRequest, HttpResponse response) throws InvocationTargetException, IllegalAccessException {
        EndpointDefinition matchedEndpoint = null;
        HashMap<String, String> pathVariables = new HashMap<>();
        for (HashMap.Entry<String, HashMap<HttpMethod, EndpointDefinition>> entry : routes.entrySet()) {
            String path = entry.getKey();
            if (UrlResolver.match( httpRequest.getRequestLine().getPath(),path)) {
                if (entry.getValue().containsKey(httpRequest.getRequestLine().getMethod())){
                    pathVariables = UrlResolver.extractPathVariables(httpRequest.getRequestLine().getPath(), path);
                    System.out.println(pathVariables);
                    matchedEndpoint = entry.getValue().get(httpRequest.getRequestLine().getMethod());
                    if (matchedEndpoint == null) {
                        log.error("HTTP Method {} does not match for path: {}", httpRequest.getRequestLine().getMethod(), httpRequest.getRequestLine().getPath());
                        throw new MethodNotMatchException("HTTP Method " + httpRequest.getRequestLine().getMethod() + " does not match for path: " + httpRequest.getRequestLine().getPath());
                    }
                    break;
                }
            }
        }
        if (matchedEndpoint == null) {
            log.error("No endpoint found for path: {}", httpRequest.getRequestLine().getPath());
            throw new ResourceNotFoundException("No endpoint found for path: " + httpRequest.getRequestLine().getPath());
        }
        List<ParameterInfo> parameterInfos = matchedEndpoint.parameters();
        Object[] args = new Object[parameterInfos.size()];
        System.out.println(httpRequest.toString());
        args[0] = httpRequest;
        args[1] = response;
        int argIndex =0;
        for (ParameterInfo parameterInfo : parameterInfos){
            if (argIndex<=1){
                argIndex++;
                continue;
            }
            System.out.println(parameterInfo.name()+" "+parameterInfo.type());
            String pathVariable = pathVariables.get(parameterInfo.name());
            args[argIndex++] = castToType(pathVariable,parameterInfo.type());
        }
        System.out.println(Arrays.toString(args));
        Method endpointMethod = matchedEndpoint.method();
        endpointMethod.setAccessible(true);
        endpointMethod.invoke(matchedEndpoint.controller(),args);
    }
    public Object castToType(String value, Type type) {
        // Get the actual class from Type
        Class<?> clazz;
        if (type instanceof Class) {
            clazz = (Class<?>) type;
        } else if (type instanceof ParameterizedType paramType) {
            clazz = (Class<?>) paramType.getRawType();
        } else {
            return value; // fallback
        }

        // Cast based on type
        if (clazz == String.class) {
            return value;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return Integer.parseInt(value);
        }
        if (clazz == long.class || clazz == Long.class) {
            return Long.parseLong(value);
        }
        if (clazz == double.class || clazz == Double.class) {
            return Double.parseDouble(value);
        }
        if (clazz == float.class || clazz == Float.class) {
            return Float.parseFloat(value);
        }
        if (clazz == boolean.class || clazz == Boolean.class) {
            return Boolean.parseBoolean(value);
        }
        if (clazz == short.class || clazz == Short.class) {
            return Short.parseShort(value);
        }
        if (clazz == byte.class || clazz == Byte.class) {
            return Byte.parseByte(value);
        }


        return value;
    }
}
