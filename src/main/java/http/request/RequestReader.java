package http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

public class RequestReader {

    public HTTPRequest readRequest(Socket socket) throws IOException {


        System.out.println("accepted new connection");
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        RequestLine requestLine = new RequestLine();
        parseRequestLine(reader, requestLine);
        System.out.println(requestLine);
        HashMap<String, String> headers = parseHeaders(reader);
        System.out.println(headers);

        String contentLengthHeader = headers.get("Content-Length");
        String body = readRequestBody(contentLengthHeader, reader);
        return new HTTPRequest(requestLine, headers, body);


    }

    private static String readRequestBody(String contentLengthHeader, BufferedReader reader) throws IOException {
        String body = null;
        if(contentLengthHeader != null) {
            int contentLength = Integer.parseInt(contentLengthHeader);
            int totalRead = 0;
            char[] buffer = new char[contentLength];
            while(totalRead < contentLength) {
                int read = reader.read(buffer, totalRead, contentLength - totalRead);
                totalRead += read;
            }
            body = new String(buffer);
        }
        return body;
    }

    private static HashMap<String, String> parseHeaders(BufferedReader reader) throws IOException {
        String line;
        HashMap<String, String> headers = new HashMap<>();
        while(!(line = reader.readLine()).isEmpty()) {
            String[] headerParts = line.split(": ");
            headers.put(headerParts[0], headerParts[1]);
        }
        return headers;
    }

    private static void parseRequestLine(BufferedReader reader, RequestLine requestLine) throws IOException {
        String line = reader.readLine();
        String[] requestLineParts = line.split(" ");
        requestLine.setMethod(requestLineParts[0]);
        requestLine.setPath(requestLineParts[1]);
        requestLine.setHttpVersion(requestLineParts[2]);
    }


}
