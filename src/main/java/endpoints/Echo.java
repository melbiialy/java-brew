package endpoints;

import http.annotation.Controller;
import http.annotation.EndPoint;
import http.annotation.PathVariable;
import http.enums.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;

import java.util.List;

@Controller(path = "/echo")
public class Echo {
    @EndPoint(method = HttpMethod.GET, path = "/{message}")
    public void echo(HttpRequest request, HttpResponse response, @PathVariable("message") String message){
        response.setBody(message);
        response.getStatusLine().setStatusCode(200);
        response.getStatusLine().setStatusMessage("OK");
        response.getHeaders().put("Content-Type", "text/plain");
    }
}
