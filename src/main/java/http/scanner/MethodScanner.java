package http.scanner;

import http.routing.endpoint.definition.Endpoint;

import java.util.List;

public interface MethodScanner {
    List<Endpoint> scan(List<Class<?>> classes) throws InstantiationException, IllegalAccessException;
}
