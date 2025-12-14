package http.response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;

public class ResponseWriter {

    public  void writeResponse(HTTPResponse response, Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
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
        String encoding = response.getHeaders().get("Content-Encoding");
        String body = response.getBody();
        byte[] encodedBody = null;
        if (encoding!=null&&encoding.contains("gzip")){
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try(GZIPOutputStream gzip = new GZIPOutputStream(bos)) {
                gzip.write(body.getBytes(StandardCharsets.UTF_8));
                gzip.finish();
                encodedBody = bos.toByteArray();
            }

        }

        httpResponse.append(statusLineStr);
        if (!headersStr.isEmpty()) {
            httpResponse.append(headersStr);
        }
        if (body != null) {

            if (encoding!=null&&encoding.contains("gzip")){
                httpResponse.append("Content-Length: "+encodedBody.length+"\r\n");
                httpResponse.append("\r\n");
            }
            else {
                httpResponse.append("Content-Length: " + body.length() + "\r\n");
                httpResponse.append("\r\n");
                httpResponse.append(body);

            }
        }

        httpResponse.append("\r\n");
        out.write(httpResponse.toString().getBytes());
        if (encodedBody!=null) {
            out.write(encodedBody);
        }
        out.flush();
    }
}
