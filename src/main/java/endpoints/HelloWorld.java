package endpoints;

import http.request.HttpRequest;
import http.response.HttpResponse;

import java.util.List;

public class HelloWorld {
    public void sayHello(HttpRequest request, HttpResponse response, List<String> pathVariables) {
       response.setBody("Hello World!");
       response.getStatusLine().setStatusCode(200);
       response.getStatusLine().setStatusMessage("OK");
       response.getHeaders().put("Content-Type", "text/plain");
       response.getHeaders().put("Content-Length", "13");
    }
}
