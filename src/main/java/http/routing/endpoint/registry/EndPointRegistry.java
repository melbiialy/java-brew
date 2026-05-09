package http.routing.endpoint.registry;


import http.bootstrap.api.Bootstrap;
import http.enums.HttpMethod;
import http.request.HttpRequest;
import http.routing.endpoint.definition.Endpoint;
import http.scanner.ClassPathScanner;
import http.scanner.MethodScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EndPointRegistry implements Registry{
    private static final Logger log = LoggerFactory.getLogger(EndPointRegistry.class);
    private final List<Endpoint> endpoints;
    private final MethodScanner methodScanner;
    private final ClassPathScanner scanner;


    public EndPointRegistry(MethodScanner methodScanner, ClassPathScanner scanner) {
        this.scanner = scanner;

        endpoints = new ArrayList<>();
        this.methodScanner = methodScanner;
    }

    @Override
    public void register(Endpoint endpoint) {
        endpoints.add(endpoint);
    }
    private void registerEndpoints(List<Endpoint> endpoints) {
        endpoints.forEach(this::register);
    }

    @Override
    public List<Endpoint> getEndpoints() {
        return Collections.unmodifiableList(endpoints);
    }
    @Override
    public Endpoint getEndPoint(HttpRequest request){

        log.trace("Request path: {}",request.getRequestLine().getPath());
        for (Endpoint endpoint : endpoints) {
            if (endpoint.matches(request.getRequestLine().getMethod(),
                    request.getRequestLine().getPath(),request.getHeaders().get("Content-Type"))) {
               Map<String ,String > pathVariables = endpoint.getPathVariables(request.getRequestLine().getPath());
                request.setPathVariables(pathVariables);
                return endpoint;
            }
        }
        return null;
    }
    public boolean anyMatchesPath(String path) {
        return endpoints.stream()
                .anyMatch(ep -> ep.getInfo().pattern().matchesPath(path));
    }
    public boolean anyMatchedPathAndMethod(String path, HttpMethod method) {
        return endpoints.stream()
                .anyMatch(ep -> ep.getInfo().pattern().matchesPath(path) && ep.getInfo().method().equals(method));
    }
    public void refresh() throws InstantiationException, IllegalAccessException {
        List<Class<?>> classes = scanner.scan();
        classes.add(Bootstrap.class);
        List<Endpoint> endPoints = methodScanner.scan(classes);
        registerEndpoints(endPoints);
    }
}
