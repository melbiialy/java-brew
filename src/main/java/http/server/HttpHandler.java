package http.server;

import http.exception.MethodNotMatchException;
import http.exception.ResourceNotFoundException;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.ResponseWriter;
import http.response.StatusLine;
import http.routing.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;


/**
 * The HttpHandler class is responsible for processing incoming HTTP requests, routing them
 * to the appropriate endpoint using the provided Router, and then writing the HTTP response
 * back to the client via the ResponseWriter.
 */
public class HttpHandler {
    private final Router router;
    private final ResponseWriter responseWriter;
    private final Logger logger = LoggerFactory.getLogger(HttpHandler.class);

    public HttpHandler(Router router, ResponseWriter responseWriter) {
        this.router = router;
        this.responseWriter = responseWriter;
    }
    public void process(HttpRequest httpRequest, Socket socket) throws IOException, InvocationTargetException, IllegalAccessException {
        HttpResponse response = new HttpResponse();
        if (httpRequest.getHeaders() != null) {
            String encoding = httpRequest.getHeaders().get("Accept-Encoding");
            String connection = httpRequest.getHeaders().get("Connection");
            if (encoding!=null&&encoding.contains("gzip")){
                response.getHeaders().put("Content-Encoding","gzip");
            }
            if (connection!=null&&connection.contains("close")){
                response.getHeaders().put("Connection","close");
            }
        }
        response.setStatusLine(new StatusLine("HTTP/1.1", 0, ""));
        try {
            router.route(httpRequest, response);
        } catch (MethodNotMatchException ex){
            response.setStatusLine(new StatusLine("HTTP/1.1", 405, "Method Not Allowed"));
        } catch (ResourceNotFoundException ex){
            response.setStatusLine(new StatusLine("HTTP/1.1", 404, "Not Found"));
        } catch (Exception ex){
            response.setStatusLine(new StatusLine("HTTP/1.1", 500, "Internal Server Error"));
        }
        this.responseWriter.writeResponse(response, socket);
        logger.info("Processed request: {} with response status: {}", httpRequest.getRequestLine(), response.getStatusLine().getStatusCode());
    }
}
