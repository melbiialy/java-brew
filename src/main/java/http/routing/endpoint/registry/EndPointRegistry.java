package http.routing.endpoint.registry;

import http.enums.HttpMethod;
import http.exception.MethodNotMatchException;
import http.exception.ResourceNotFoundException;
import http.request.HttpRequest;
import http.routing.endpoint.definition.Endpoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EndPointRegistry implements Registry{
    public static EndPointRegistry instance;
    private final List<Endpoint> endpoints;


    private EndPointRegistry() {
        endpoints = new ArrayList<>();
    }

    @Override
    public void register(Endpoint endpoint) {
        endpoints.add(endpoint);
    }

    @Override
    public List<Endpoint> getEndpoints() {
        return Collections.unmodifiableList(endpoints);
    }
    @Override
    public Endpoint getEndPoint(HttpRequest request){
        boolean isMatchPath = false;
        for (Endpoint endpoint : endpoints) {
            Map<String, String> pathVariables = endpoint.getInfo()
                    .pattern()
                    .match(request.getRequestLine().getPath());
            if (pathVariables == null) continue;

            if (!endpoint.getInfo().method().equals(request.getRequestLine().getMethod())) {
                isMatchPath = true;
                continue;
            }

            request.setPathVariables(pathVariables);

            return endpoint;
        }
        if (isMatchPath) {
            throw new MethodNotMatchException("HTTP method not allowed: " + request.getRequestLine().getMethod() + " for path: " + request.getRequestLine().getPath());
        }
        throw new ResourceNotFoundException("No endpoint found for path: " + request.getRequestLine().getPath());
    }



    public synchronized static EndPointRegistry getInstance() {
        if (instance == null) {
            instance = new EndPointRegistry();
        }
        return instance;
    }
}
