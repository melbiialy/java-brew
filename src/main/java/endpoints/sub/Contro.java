package endpoints.sub;

import http.annotation.Controller;
import http.annotation.EndPoint;

@Controller(path = "/sub")
public class Contro {
    @EndPoint(path = "/test", method = http.enums.HttpMethod.GET)
    public void test() {
        System.out.println("test");
    }
}
