package endpoints;

import http.request.HTTPRequest;
import http.response.HTTPResponse;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.util.List;

public class FileEndPoint{
    public void files(HTTPRequest request, HTTPResponse response, List<String> pathVariables) throws IOException {
        String filename = pathVariables.getFirst();
        File file = new File("/home/bialy/codecrafters-http-server-java/src/temp/" + filename);

        if (file.exists()) {
            response.setBody(Files.readString(file.toPath()));
            response.getStatusLine().setStatusCode(200);
            response.getStatusLine().setStatusMessage("OK");
            response.getHeaders().put("Content-Type", "application/octet-stream");
            response.getHeaders().put("Content-Length", String.valueOf(file.length()));
        }
        else {
            response.getStatusLine().setStatusCode(404);
            response.getStatusLine().setStatusMessage("Not Found");
        }
//        System.out.println(Files.readString(file.toPath()));
    }
}
