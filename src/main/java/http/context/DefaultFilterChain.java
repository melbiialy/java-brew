package http.context;

import http.request.HttpRequest;
import http.response.HttpResponse;
import http.server.HttpFinalHandler;

public class DefaultFilterChain implements FilterChain{
    private final FilterContext context;
    private final HttpFinalHandler handler;

    public DefaultFilterChain(FilterContext context, HttpFinalHandler handler) {
        this.context = context;
        this.handler = handler;
    }
    @Override
    public void doChain(HttpRequest request, HttpResponse response) throws Exception {
        BaseFilter nextBaseFilter = context.getNextFilter();
        if (nextBaseFilter != null) {
            nextBaseFilter.doFilter(request, response, this);
        }else {
            handler.handle(request, response);
            context.set();
        }

    }
}
