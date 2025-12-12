package http.server;

import exception.ResourceNotFoundException;
import http.request.HTTPRequest;
import http.request.RequestReader;
import http.response.HTTPResponse;
import http.response.HTTPResponseBuilder;
import http.response.ResponseWriter;
import http.routing.Router;

import java.io.BufferedReader;
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
        this.running = true;
    }
    public void start() throws Exception {
        System.out.println("Server started and listening on port " + serverSocket);
        System.out.println("Press CTRL+C to stop the server");
        while (this.running){
            Socket socket = serverSocket.accept();
            try {
                HTTPRequest httpRequest = requestReader.readRequest(socket);
                Thread thread = new Thread(()->{
                    try {
                        process(httpRequest, socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                thread.start();


            }catch (Exception e){
                System.out.println("Exception: " + e.getMessage());
            }

        }
    }

    private void process(HTTPRequest httpRequest, Socket socket) throws IOException {
        try {
            System.out.println(httpRequest);
            Object responseBody = router.route(httpRequest);
            HTTPResponseBuilder responseBuilder = new HTTPResponseBuilder();
            HTTPResponse response = responseBuilder.withBody(responseBody.toString())
//                            .withHeader("Content-Length", String.valueOf(responseBody.toString().length()))
//                            .withHeader("Content-Type", "text/plain")
                    .withStatusLine(new http.response.StatusLine("HTTP/1.1", 200, "OK"))
                    .build();

            ResponseWriter.writeResponse(response, socket);
        }catch (ResourceNotFoundException ex){
            HTTPResponseBuilder responseBuilder = new HTTPResponseBuilder();
            HTTPResponse response = responseBuilder.withStatusLine(new http.response.StatusLine("HTTP/1.1", 404, "Not Found"))
                    .build();
            ResponseWriter.writeResponse(response, socket);
        }
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
