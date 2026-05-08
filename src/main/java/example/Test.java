package example;

import http.annotation.Controller;
import http.annotation.EndPoint;
import http.enums.ContentType;
import http.enums.HttpMethod;
import http.response.HttpResponse;

@Controller
public class Test {
    @EndPoint(path = "/test",method = HttpMethod.GET,consumes = "application/json",produces = "application/json")
    public void test(HttpResponse response) {
        response.setBody("Hello");
        response.getHeaders().put("Content-Type", ContentType.APPLICATION_JSON.toString());
        response.getStatusLine().setStatusCode(200);
    }
}
