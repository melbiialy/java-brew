package endpoints;

import http.request.HTTPRequest;
import http.response.HTTPResponse;

import java.util.List;

public class Echo {
    public void echo(HTTPRequest request, HTTPResponse response, List<String> pathVariables){
        response.setBody(pathVariables.getFirst());
        response.getStatusLine().setStatusCode(200);
        response.getStatusLine().setStatusMessage("OK");
        response.getHeaders().put("Content-Type", "text/plain");
        response.getHeaders().put("Content-Length", String.valueOf(pathVariables.getFirst().length()));
    }
}
