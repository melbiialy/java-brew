package http.response;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ResponseWriter {

    public static void writeResponse(HTTPResponse response, Socket socket) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream());
//        StatusLine statusLine = response.getStatusLine();
//        String statusLineStr = String.format("%s %d %s\r\n\r\n",
//                                        statusLine.getHttpVersion(),
//                                        statusLine.getStatusCode(),
//                                        statusLine.getStatusMessage());
//        out.println(statusLineStr);
//        response.getHeaders().forEach((key, value) -> out.println(key + ": " + value));
//        out.print("\r\n");
        out.println("HTTP/1.1 200 OK\r\n\r\n");

        out.flush();
    }
}
