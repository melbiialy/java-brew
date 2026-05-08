package http.scanner;

import http.annotation.EndPoint;
import http.annotation.PathVariable;
import http.enums.ContentType;
import http.enums.HttpMethod;
import http.routing.endpoint.definition.*;
import http.routing.endpoint.path.PathPattern;
import http.routing.endpoint.registry.EndPointRegistry;
import http.routing.endpoint.registry.Registry;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class EndpointScanner implements MethodScanner{
    @Override
    public void scan(List<Class<?>> classes) {
        Registry registry = EndPointRegistry.getInstance();
        for (Class<?> clazz : classes) {
            registerEndPoints(clazz,registry);
        }
    }

    private void registerEndPoints(Class<?> clazz,Registry registry) {
        Method[] methods = clazz.getMethods();
        for(Method method : methods){
            if (method.isAnnotationPresent(EndPoint.class)) {
                Endpoint endpoint = getEndpoint(method,clazz);
                registry.register(endpoint);
            }
        }
    }

    private Endpoint getEndpoint(Method method, Class<?> clazz) {
        EndPoint endPoint = method.getAnnotation(EndPoint.class);
        EndPointInfo info = getEndPointInfo(endPoint);
        HandlerMethod handlerMethod = getHandlerMethod(method, clazz);
        Parameter[] parameters = method.getParameters();
        ParameterDescriptor[] parameterDescriptors = new ParameterDescriptor[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> type = parameter.getType();

            if (parameter.isAnnotationPresent(PathVariable.class)) {
                String name = parameter.getAnnotation(PathVariable.class).value();
                parameterDescriptors[i] = new ParameterDescriptor(i, type, ParamSource.PATH, name, true);

            }
        }

        return new Endpoint(info, handlerMethod, parameterDescriptors, null);
    }

    private static HandlerMethod getHandlerMethod(Method method, Class<?> clazz) {
        return new HandlerMethod(clazz, method);
    }

    private static EndPointInfo getEndPointInfo(EndPoint endPoint) {
        HttpMethod httpMethod = endPoint.method();
        String path = endPoint.path();
        PathPattern pathPattern = new PathPattern(path);
        ContentType consume = endPoint.consumes();
        ContentType produces = endPoint.produces();
        return new EndPointInfo(httpMethod, pathPattern, consume.name(), produces.name());
    }

}
