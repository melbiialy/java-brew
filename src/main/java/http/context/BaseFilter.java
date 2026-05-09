package http.context;

import http.request.HttpRequest;
import http.response.HttpResponse;

public interface BaseFilter {
    void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) throws Exception;
}
