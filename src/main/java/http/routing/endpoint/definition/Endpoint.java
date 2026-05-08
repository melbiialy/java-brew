package http.routing.endpoint.definition;

import http.enums.HttpMethod;
import http.request.HttpRequest;

import java.util.Map;
import java.util.UUID;

public class Endpoint {
    private final EndPointInfo info;
    private final HandlerMethod handler;
    private final ParameterDescriptor[] parameters;
    private final ResponseDescriptor responseDescriptor;

    public Endpoint(EndPointInfo info, HandlerMethod handler, ParameterDescriptor[] parameters, ResponseDescriptor responseDescriptor) {
        this.info = info;
        this.handler = handler;
        this.parameters = parameters;
        this.responseDescriptor = responseDescriptor;
    }
    public boolean matches(HttpMethod method, String path) {
        return info.method().equals(method)
                &&info.pattern().matchesPath(path);
    }
    public Map<String, String> getPathVariables(String path) {
        return info.pattern().match(path);
    }

    public Object invoke(HttpRequest request, Map<String, String> pathVars)
            throws Exception {
        Object[] args = resolveArgs(request, pathVars);
        return handler.method().invoke(handler.bean(), args);
    }

    private Object[] resolveArgs(HttpRequest request, Map<String, String> pathVars) {
        Object[] args = new Object[parameters.length];

        for (ParameterDescriptor parameter : parameters) {

            if (pathVars.containsKey(parameter.name())) {
                String pathVariable = pathVars.get(parameter.name());
                handlePathVar(args,parameter,pathVariable);
            }else if (parameter.type() == HttpRequest.class) {
                args[parameter.index()] = request;
            }

        }
        return args;
    }

    private void handlePathVar(Object[] args, ParameterDescriptor parameter, String pathVariable) {
        if (pathVariable == null) {
            if (parameter.required()) {
                throw new IllegalArgumentException(
                        "Missing required path variable: " + parameter.name()
                );
            }
            args[parameter.index()] = null;
            return;
        }

        args[parameter.index()] = convert(pathVariable, parameter.type());
    }
    private Object convert(String raw, Class<?> type) {
        if (type == String.class)               return raw;
        if (type == Long.class
                || type == long.class)                 return Long.parseLong(raw);
        if (type == Integer.class
                || type == int.class)                  return Integer.parseInt(raw);
        if (type == Double.class
                || type == double.class)               return Double.parseDouble(raw);
        if (type == Boolean.class
                || type == boolean.class)              return Boolean.parseBoolean(raw);
        if (type == UUID.class)                 return UUID.fromString(raw);
        throw new IllegalArgumentException(
                "No converter for type '" + type.getName() + "' on path variable '" + raw + "'"
        );
    }
    public EndPointInfo getInfo() {
        return info;
    }
    public HandlerMethod getHandler() {
        return handler;
    }
    public ParameterDescriptor[] parameters() {
        return parameters;
    }
    public ResponseDescriptor getResponseDescriptor() {
        return responseDescriptor;
    }
}
