package endpoints;

import http.request.HttpRequest;
import http.response.HttpResponse;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.util.List;

public class FileEndPoint{
    public void files(HttpRequest request, HttpResponse response, List<String> pathVariables) throws IOException {
        String filename = pathVariables.getFirst();
        File file = new File("/tmp/data/codecrafters.io/http-server-tester/" + filename);

        if (file.exists()) {
            response.setBody(Files.readString(file.toPath()));
            response.getStatusLine().setStatusCode(200);
            response.getStatusLine().setStatusMessage("OK");
            response.getHeaders().put("Content-Type", "application/octet-stream");
        }
        else {
            response.getStatusLine().setStatusCode(404);
            response.getStatusLine().setStatusMessage("Not Found");
        }
//        System.out.println(Files.readString(file.toPath()));
    }
    public void postFile(HttpRequest request, HttpResponse response, List<String> pathVariables) throws IOException {
        String filename = pathVariables.getFirst();
        File file = new File("/tmp/data/codecrafters.io/http-server-tester/" + filename);
        Files.writeString(file.toPath(), request.getBody());
        response.getStatusLine().setStatusCode(201);
        response.getStatusLine().setStatusMessage("Created");
        System.out.println("File created");
    }
}
