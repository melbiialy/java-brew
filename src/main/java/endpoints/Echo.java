package endpoints;

import http.annotation.Controller;
import http.annotation.EndPoint;
import http.enums.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;

import java.util.List;

@Controller(path = "/echo/{message}")
public class Echo {
    @EndPoint(method = HttpMethod.GET, path = "/echo/{message}")
    public void echo(HttpRequest request, HttpResponse response, List<String> pathVariables){
        response.setBody(pathVariables.getFirst());
        response.getStatusLine().setStatusCode(200);
        response.getStatusLine().setStatusMessage("OK");
        response.getHeaders().put("Content-Type", "text/plain");
    }
}
