package http.server;
import http.request.HTTPRequest;
import http.request.RequestReader;
import http.response.HTTPResponse;
import http.response.ResponseWriter;
import http.response.StatusLine;
import http.routing.Router;

import java.io.IOException;
import java.net.Socket;

public class HTTPServer {
    private HTTPServerSocket serverSocket;
    private RequestReader requestReader;
    private ResponseWriter responseWriter;
    private boolean running;
    private Router router;

    public HTTPServer(int port) throws Exception {
        serverSocket = new HTTPServerSocket(port);
        requestReader = new RequestReader();
        responseWriter = new ResponseWriter();
        router = new Router();
        running = true;

    }
    public void start() throws Exception {
        System.out.println("Server started and listening on port " + serverSocket);
        System.out.println("Press CTRL+C to stop the server");
        while (this.running){
            Socket socket = serverSocket.accept();
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(0);
            socket.setKeepAlive(true);
            System.out.println("accepted new connection");
            Thread thread = new Thread(()-> {
                try {

                    HTTPRequest httpRequest ;
                    while ((httpRequest = requestReader.readRequest(socket))!=null) {
                        boolean closeConnection = httpRequest.getHeaders().get("Connection").equals("close");

                        process(httpRequest, socket);
                        if (closeConnection) {
                            socket.close();
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.start();
        }
    }
    public void stop() {
        this.running = false;
    }

    private void process(HTTPRequest httpRequest, Socket socket) throws IOException {
        HTTPResponse response = new HTTPResponse();
        if (httpRequest.getHeaders() != null) {
            String encoding = httpRequest.getHeaders().get("Accept-Encoding");
//            String[] acceptEncoding = encoding.split(",");
            if (encoding!=null&&encoding.contains("gzip")){
                response.getHeaders().put("Content-Encoding","gzip");
            }
        }
        response.setStatusLine(new StatusLine("HTTP/1.1", 0, ""));
        router.route(httpRequest,response);
        this.responseWriter.writeResponse(response, socket);
        System.out.println("Response sent to client");
    }

    public HTTPServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(HTTPServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public RequestReader getRequestReader() {
        return requestReader;
    }

    public void setRequestReader(RequestReader requestReader) {
        this.requestReader = requestReader;
    }

    public ResponseWriter getResponseWriter() {
        return responseWriter;
    }

    public void setResponseWriter(ResponseWriter responseWriter) {
        this.responseWriter = responseWriter;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Router getRouter() {
        return router;
    }

    public void setRouter(Router router) {
        this.router = router;
    }
}
