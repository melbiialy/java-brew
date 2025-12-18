package http.server;

import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.ResponseWriter;
import http.response.StatusLine;
import http.routing.Router;

import java.io.IOException;
import java.net.Socket;

public class HttpHandler {
    private final Router router;
    private final ResponseWriter responseWriter;
    public HttpHandler(Router router, ResponseWriter responseWriter) {
        this.router = router;
        this.responseWriter = responseWriter;
    }
    public void process(HttpRequest httpRequest, Socket socket) throws IOException {
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
//        router.route(httpRequest,response);
        this.responseWriter.writeResponse(response, socket);
        System.out.println("Response sent to client");
    }
}
