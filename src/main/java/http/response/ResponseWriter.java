package http.response;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ResponseWriter {

    public  void writeResponse(HTTPResponse response, Socket socket) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        StatusLine statusLine = response.getStatusLine();
        String statusLineStr = String.format("%s %d %s\r\n",
                                        statusLine.getHttpVersion(),
                                        statusLine.getStatusCode(),
                                        statusLine.getStatusMessage());
        StringBuilder httpResponse = new StringBuilder();
        StringBuilder headersStr = new StringBuilder();
        if (response.getHeaders() != null) {
             response.getHeaders().forEach((key, value) -> headersStr.append(key).append(": ").append(value).append("\r\n"));
         }
        String body = response.getBody();
        httpResponse.append(statusLineStr);
        if (!headersStr.isEmpty()) {
            httpResponse.append(headersStr);
        }
        if (body != null) {
            httpResponse.append("\r\n").append(body);
        }
        out.print(httpResponse.toString());
        out.flush();
    }
}
