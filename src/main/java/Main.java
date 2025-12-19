import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import endpoints.*;

import http.request.HttpRequest;
import http.response.HttpResponse;
import http.routing.EndPointRegistry;
import http.routing.Router;
import http.server.HttpServer;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.INFO);

       HttpServer httpServer = new HttpServer();
       httpServer.start();



}
}
