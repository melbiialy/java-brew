package http.routing;

import java.lang.reflect.Method;

public class Route<T> {
    private Class<T> controller;
    private Method method;
    private String httpMethod;
    public Route(Class<T> controller, Method method, String httpMethod) {
        this.controller = controller;
        this.method = method;
        this.httpMethod = httpMethod;
    }

    public Class<T> getController() {
        return controller;
    }

    public void setController(Class<T> controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }
}
