package endpoints;

import http.request.HTTPRequest;
import http.response.HTTPResponse;

import java.util.List;

public class UserAgent {
    public void userAgent(HTTPRequest request, HTTPResponse response, List<String> pathVariables){
        response.setBody(request.getHeaders().get("User-Agent"));
        response.getStatusLine().setStatusCode(200);
        response.getStatusLine().setStatusMessage("OK");
        response.getHeaders().put("Content-Type", "text/plain");
        response.getHeaders().put("Content-Length", String.valueOf(request.getHeaders().get("User-Agent").length()));

    }
}
