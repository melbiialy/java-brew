package http.bootstrap.filter;

import http.annotation.Filter;
import http.context.BaseFilter;
import http.context.FilterChain;
import http.request.HttpRequest;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Filter
public class LoggerFilter implements BaseFilter {
    Logger logger = LoggerFactory.getLogger(LoggerFilter.class);
    @Override
    public void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) throws Exception {
        logger.info("Request: {}", request.getRequestLine().toString());
        chain.doChain(request,response);
    }
}
