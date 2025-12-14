package http.response;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.net.Socket;
import java.nio.charset.StandardCharsets;

import java.util.zip.GZIPOutputStream;


public class ResponseWriter {

    public void writeResponse(HTTPResponse response, Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        StatusLine statusLine = response.getStatusLine();

        String encoding = response.getHeaders().get("Content-Encoding");
        byte[] encodedBody;

        if (encoding != null && encoding.equals("gzip")) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try (GZIPOutputStream gzip = new GZIPOutputStream(buffer)) {
                gzip.write(response.getBody().getBytes(StandardCharsets.UTF_8));
            }
            encodedBody = buffer.toByteArray();
        } else {
            encodedBody = response.getBody().getBytes(StandardCharsets.UTF_8);
        }

        String statusLineString = statusLine.getHttpVersion() + " " +
                statusLine.getStatusCode() + " " +
                statusLine.getStatusMessage() + "\r\n";
        out.write(statusLineString.getBytes());

        response.getHeaders().forEach((key, value) -> {
            try {
                String headerLine = key + ": " + value + "\r\n";
                out.write(headerLine.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        out.write(("Content-Length: " + encodedBody.length + "\r\n").getBytes());

        out.write("\r\n".getBytes());

        out.write(encodedBody);
        out.flush();
    }
}
