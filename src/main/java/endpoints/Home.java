package endpoints;

import http.request.HTTPRequest;
import http.response.HTTPResponse;

import java.util.List;

public class Home {
    public void home(HTTPRequest request, HTTPResponse response, List<String> pathVariables){
        response.getStatusLine().setStatusCode(200);
        response.getStatusLine().setStatusMessage("OK");
    }
}
