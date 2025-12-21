import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;



import http.server.HttpServer;
import org.slf4j.LoggerFactory;



public class JavaBrew {

    public static void main(String[] args) throws Exception {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.INFO);

       HttpServer httpServer = new HttpServer();
       httpServer.start();



}
}
