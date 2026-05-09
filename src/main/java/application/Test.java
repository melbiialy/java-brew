package application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import http.annotation.Controller;
import http.annotation.EndPoint;
import http.enums.ContentType;
import http.enums.HttpMethod;
import http.response.HttpResponse;

@Controller(path = "/api")
public class Test {
    @EndPoint(path = "/test",method = HttpMethod.GET)
    public void test(HttpResponse response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        response.setBody(mapper.writeValueAsString("hello world"));
        response.getHeaders().put("Content-Type", ContentType.APPLICATION_JSON.toString());
        response.getStatusLine().setStatusCode(200);
    }
}
