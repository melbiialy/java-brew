package http.routing.endpoint.registry;


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

        for (Endpoint endpoint : endpoints) {
            if (endpoint.matches(request.getRequestLine().getMethod(),
                    request.getRequestLine().getPath(),request.getHeaders().get("content-type"))) {
               Map<String ,String > pathVariables = endpoint.getPathVariables(request.getRequestLine().getPath());
                request.setPathVariables(pathVariables);
                return endpoint;
            }
        }
        return null;
    }



    public synchronized static EndPointRegistry getInstance() {
        if (instance == null) {
            instance = new EndPointRegistry();
        }
        return instance;
    }
}
