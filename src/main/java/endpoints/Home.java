package endpoints;

import http.request.HttpRequest;
import http.response.HttpResponse;

import java.util.List;

public class Home {
    public void home(HttpRequest request, HttpResponse response, List<String> pathVariables){
        response.getStatusLine().setStatusCode(200);
        response.getStatusLine().setStatusMessage("OK");
    }
}
