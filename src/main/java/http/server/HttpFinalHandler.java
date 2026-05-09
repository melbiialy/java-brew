package http.server;

import http.request.HttpRequest;
import http.response.HttpResponse;
import http.routing.Router;
import http.routing.endpoint.definition.Endpoint;

public class HttpFinalHandler {
    private final Router router;
    public HttpFinalHandler(Router router) {
        this.router = router;
    }
    public void handle(HttpRequest request, HttpResponse response) throws Exception {
        Endpoint endpoint = router.route(request);
        endpoint.invoke(request, response, request.getPathVariables());
        if (!response.getHeaders().containsKey("Content-Type")
                && endpoint.getInfo().produces() != null) {
            response.getHeaders().put("Content-Type", endpoint.getInfo().produces().value);
        }
    }
}
