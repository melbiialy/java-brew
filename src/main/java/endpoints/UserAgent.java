package endpoints;

import http.request.HttpRequest;
import http.response.HttpResponse;

import java.util.List;

public class UserAgent {
    public void userAgent(HttpRequest request, HttpResponse response, List<String> pathVariables){
        response.setBody(request.getHeaders().get("User-Agent"));
        response.getStatusLine().setStatusCode(200);
        response.getStatusLine().setStatusMessage("OK");
        response.getHeaders().put("Content-Type", "text/plain");

    }
}
