package http.routing.endpoint.registry;

import http.enums.HttpMethod;
import http.request.HttpRequest;
import http.routing.endpoint.definition.Endpoint;

import java.util.List;

public interface Registry {
    void register(Endpoint endpoint);
    List<Endpoint> getEndpoints();
    Endpoint getEndPoint(HttpRequest request);
    boolean anyMatchesPath(String path);
    boolean anyMatchedPathAndMethod(String path, HttpMethod method);
}
