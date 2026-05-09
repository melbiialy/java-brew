package http.context;

import http.request.HttpRequest;
import http.response.HttpResponse;

public interface FilterChain {
    void doChain(HttpRequest request, HttpResponse response) throws Exception;
}
