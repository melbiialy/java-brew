package http.routing.endpoint.definition;

import java.lang.reflect.Method;

public record HandlerMethod(Object bean, Method method) {
}
