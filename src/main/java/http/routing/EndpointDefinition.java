package http.routing;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;

public class EndpointDefinition {
    private final Object controller;
    private final Method method;
    private final HashMap<String, Type> parameters;

    public EndpointDefinition(Object controller, Method method, HashMap<String, Type> parameters) {
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
    public HashMap<String, Type> getParameters() {
        return parameters;
    }
}
