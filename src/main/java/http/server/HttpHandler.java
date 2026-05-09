package http.server;

import http.context.FilterChain;
import http.exception.MethodNotMatchException;
import http.exception.ResourceNotFoundException;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.ResponseWriter;
import http.response.StatusLine;
import http.routing.Router;
import http.routing.endpoint.definition.Endpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;


public class HttpHandler {
    private final FilterChain filterChain;
    private final ResponseWriter responseWriter;
    private final Logger logger = LoggerFactory.getLogger(HttpHandler.class);

    public HttpHandler(FilterChain filterChain, ResponseWriter responseWriter) {
        this.filterChain = filterChain;
        this.responseWriter = responseWriter;
    }

    public void process(HttpRequest httpRequest, Socket socket) throws Exception {
        HttpResponse response = new HttpResponse();
        filterChain.doChain(httpRequest, response);



        this.responseWriter.writeResponse(response, socket);
    }


}
