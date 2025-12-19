package http.routing;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class EndpointDefinition {
    private final Object controller;
    private final Method method;
    private final List<ParameterInfo> parameters;

    public EndpointDefinition(Object controller, Method method, List<ParameterInfo> parameters) {
        this.controller = controller;
        this.method = method;
        this.parameters = parameters;
    }
    public Object getController() {
        return controller;
    }
    public Method getMethod() {
        return method;
    }
    public List<ParameterInfo> getParameters() {
        return parameters;
    }
}
