import endpoints.HelloWorld;
import http.request.HTTPRequest;
import http.routing.Route;
import http.routing.Router;
import http.server.HTTPServer;
import http.server.HTTPServerSocket;
import http.request.RequestReader;
import http.response.HTTPResponse;
import http.response.HTTPResponseBuilder;
import http.response.ResponseWriter;
import http.response.StatusLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class Main {
  public static void main(String[] args) throws Exception {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    // Uncomment this block to pass the first stage
    //
      while (true) {
          try(
                  ServerSocket serverSocket = new ServerSocket(4221);) {

//
//               Since the tester restarts your program quite often, setting SO_REUSEADDR
//               ensures that we don't run into 'Address already in use' errors
              serverSocket.setReuseAddress(true);

              try {
                  Socket socket = serverSocket.accept();

                  new Thread((()-> {
                      try {
                          extracted(socket);
                      } catch (IOException e) {
                          throw new RuntimeException(e);
                      }finally {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                      }
                  })).start();
              } catch (IOException e) {
                  throw new RuntimeException(e);
              }

          } catch (IOException e) {
              System.out.println("IOException: " + e.getMessage());
          }
      }


//      HTTPServer server = new HTTPServer(4221);
//      Route<HelloWorld> route = new Route<>(HelloWorld.class,HelloWorld.class.getMethod("sayHello"),"GET");
//      Router router = server.getRouter();
//      router.addEndPoint("/",route);
//      server.start();
  }

    private static void extracted(Socket socket) throws IOException {
        // Wait for connection from client.
        System.out.println("accepted new connection");
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        String request = in.readLine();
        String path = request.split(" ")[1];
        System.out.println(request);

        String response;
        if (path.equals("/")) {
            response = "HTTP/1.1 200 OK\r\n\r\n";
            out.println(response);
            out.flush();
            return;
        }

        String endpointPath = path.split("/")[1];
        if (endpointPath.equals("user-agent")){
            System.out.println(in.readLine());

            String userAgent = in.readLine();
            userAgent = userAgent.substring("User-Agent: ".length());
            response = String.format("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: %d\r\n\r\n%s", userAgent.length(), userAgent);
        }
        else
        if (Objects.equals(endpointPath, "echo")) {

            String endpoint = path.split("/")[2];
            System.out.println("endpoint: " + endpoint);
            response = String.format("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: %d\r\n\r\n%s", endpoint.length(), endpoint);

        }  else {
            response = "HTTP/1.1 404 Not Found\r\n\r\n";
        }

        out.println(response);
        out.flush();
        System.out.println("responseString: " + response);
    }
}
