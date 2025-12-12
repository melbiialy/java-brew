import endpoints.Echo;
import endpoints.FileEndPoint;

import endpoints.HelloWorld;
import endpoints.Home;
import http.request.HTTPRequest;
import http.response.HTTPResponse;
import http.routing.Route;
import http.routing.Router;
import http.server.HTTPServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws Exception {

//        System.out.println("Starting server on port 4221");
//
//        ServerSocket serverSocket = new ServerSocket(4221);
//        serverSocket.setReuseAddress(true);
//
//        while (true) {
//            Socket socket = serverSocket.accept();
//            new Thread(() -> {
//                try {
//                    handle(socket);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        }
//    }
//
//    private static void handle(Socket socket) throws IOException {
//        System.out.println("accepted new connection");
//
//        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//
//        String requestLine = in.readLine();
//        if (requestLine == null) return;
//
//        System.out.println(requestLine);
//        String path = requestLine.split(" ")[1];
//
//        // Read headers until blank line
//        String line;
//        String userAgent = null;
//
//        while (!(line = in.readLine()).equals("")) {
//            if (line.startsWith("User-Agent:")) {
//                userAgent = line.substring("User-Agent: ".length());
//            }
//        }
//
//        String response;
//
//        // root
//        if (path.equals("/")) {
//            response = "HTTP/1.1 200 OK\r\n\r\n";
//            out.print(response);
//            out.flush();
//            return;
//        }
//
//        // echo
//        String endpointPath = path.split("/")[1];
//
//        if (endpointPath.equals("echo")) {
//            String msg = path.substring("/echo/".length());
//            response = String.format(
//                    "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: %d\r\n\r\n%s",
//                    msg.length(), msg
//            );
//            out.print(response);
//            out.flush();
//            return;
//        }
//
//        // user-agent
//        if (endpointPath.equals("user-agent")) {
//            if (userAgent == null) userAgent = "";
//            response = String.format(
//                    "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: %d\r\n\r\n%s",
//                    userAgent.length(), userAgent
//            );
//            out.print(response);
//            out.flush();
//            return;
//        }
//
//        // not found
//        response = "HTTP/1.1 404 Not Found\r\n\r\n";
//        out.print(response);
//        out.flush();
//    }
        HTTPServer server = new HTTPServer(4221);
        Route<HelloWorld> helloWorldRoute = new Route<>(HelloWorld.class, HelloWorld.class.getDeclaredMethod("sayHello", HTTPRequest.class, HTTPResponse.class,List.class),"GET");
        Route<Home> homeRoute = new Route<>(Home.class, Home.class.getDeclaredMethod("home", HTTPRequest.class, HTTPResponse.class, List.class),"GET");
        Router router = server.getRouter();
        router.addEndPoint("/hello",helloWorldRoute);
        router.addEndPoint("/",homeRoute);
        Route<Echo> echoRoute = new Route<>(Echo.class, Echo.class.getDeclaredMethod("echo", HTTPRequest.class, HTTPResponse.class,List.class),"GET");
        router.addEndPoint("/echo/{msg}",echoRoute);
        Route<FileEndPoint> route = new Route<>(FileEndPoint.class, FileEndPoint.class.getDeclaredMethod("files", HTTPRequest.class, HTTPResponse.class,List.class),"GET");
        router.addEndPoint("/files/{path}",route);
        server.start();


}
}
