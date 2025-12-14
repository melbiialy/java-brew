package http.response;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.net.Socket;
import java.util.HashMap;
import java.util.zip.GZIPOutputStream;


public class ResponseWriter {

    public  void writeResponse(HTTPResponse response, Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        StatusLine statusLine = response.getStatusLine();
        String statusLineString = statusLine.getHttpVersion() + " " + statusLine.getStatusCode() + " " + statusLine.getStatusMessage() + "\r\n";
        out.write(statusLineString.getBytes());
        out.write("\r\n".getBytes());
        String encoding = response.getHeaders().get("Content-Encoding");
        response.getHeaders().forEach(
                (key, value) -> {
                    try {
                        String headerLine = key + ": " + value + "\r\n";
                        out.write(headerLine.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        byte[] encodedBody = null;
        if (encoding != null && encoding.equals("gzip")) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(buffer);
            gzip.write(response.getBody().getBytes());
            gzip.close();
            encodedBody = buffer.toByteArray();

        } else {
            encodedBody = response.getBody().getBytes();
        }
        out.write(String.format("Content-Length: %d\r\n\r\n", encodedBody.length).getBytes());
        out.write(encodedBody);
        out.flush();


    }
}
