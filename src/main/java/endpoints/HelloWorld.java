package endpoints;

import http.request.HTTPRequest;
import http.response.HTTPResponse;

import java.util.List;

public class HelloWorld {
    public void sayHello(HTTPRequest request, HTTPResponse response, List<String> pathVariables) {
       response.setBody("Hello World!");
       response.getStatusLine().setStatusCode(200);
       response.getStatusLine().setStatusMessage("OK");
       response.getHeaders().put("Content-Type", "text/plain");
       response.getHeaders().put("Content-Length", "13");
    }
}
