package http.scanner;

import http.annotation.Controller;
import http.annotation.EndPoint;
import http.annotation.PathVariable;
import http.enums.ContentType;
import http.enums.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.routing.endpoint.definition.*;
import http.routing.endpoint.path.PathPattern;
import http.routing.endpoint.registry.EndPointRegistry;
import http.routing.endpoint.registry.Registry;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class EndpointScanner implements MethodScanner{
    private static  EndpointScanner instance;
    public static EndpointScanner getInstance() {
        if (instance == null) {
            synchronized (EndpointScanner.class) {
                if (instance == null) {
                    instance = new EndpointScanner();
                }
            }
        }
        return instance;
    }

    @Override
    public List<Endpoint> scan(List<Class<?>> classes) throws InstantiationException, IllegalAccessException {
        List<Endpoint> endpoints = new ArrayList<>();
        for (Class<?> clazz : classes) {
            registerEndPoints(clazz,endpoints);
        }
        return endpoints;
    }

    private void registerEndPoints(Class<?> clazz,List<Endpoint> endpoints) throws InstantiationException, IllegalAccessException {
        Method[] methods = clazz.getMethods();
        for(Method method : methods){
            if (method.isAnnotationPresent(EndPoint.class)) {
                Endpoint endpoint = getEndpoint(method,clazz);
                endpoints.add(endpoint);
            }
        }
    }

    private Endpoint getEndpoint(Method method, Class<?> clazz) throws InstantiationException, IllegalAccessException {
        String prefix = clazz.getAnnotation(Controller.class).path();
        EndPoint endPoint = method.getAnnotation(EndPoint.class);
        EndPointInfo info = getEndPointInfo(endPoint,prefix);
        HandlerMethod handlerMethod = getHandlerMethod(method, clazz);
        Parameter[] parameters = method.getParameters();
        ParameterDescriptor[] parameterDescriptors = new ParameterDescriptor[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> type = parameter.getType();

            if (parameter.isAnnotationPresent(PathVariable.class)) {
                String name = parameter.getAnnotation(PathVariable.class).value();
                parameterDescriptors[i] = new ParameterDescriptor(i, type, ParamSource.PATH, name, true);
            } else if (type == HttpRequest.class) {
                parameterDescriptors[i] = new ParameterDescriptor(i, type, null, null, false);
            } else if (type == HttpResponse.class) {
                parameterDescriptors[i] = new ParameterDescriptor(i, type, null, null, false);
            } else {
                parameterDescriptors[i] = new ParameterDescriptor(i, type, null, parameter.getName(), false);
            }
        }

        return new Endpoint(info, handlerMethod, parameterDescriptors, null);
    }

    private static HandlerMethod getHandlerMethod(Method method, Class<?> clazz) throws InstantiationException, IllegalAccessException {
        return new HandlerMethod(clazz.newInstance(), method);
    }

    private static EndPointInfo getEndPointInfo(EndPoint endPoint, String prefix) {
        HttpMethod httpMethod = endPoint.method();
        String path = prefix+endPoint.path();
        PathPattern pathPattern = new PathPattern(path);
        ContentType  consume = endPoint.consumes();
        ContentType  produces = endPoint.produces();
        return new EndPointInfo(httpMethod, pathPattern, consume, produces);
    }

}
