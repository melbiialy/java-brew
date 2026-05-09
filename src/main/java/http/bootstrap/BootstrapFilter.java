package http.bootstrap;

import http.annotation.Filter;
import http.context.BaseFilter;
import http.context.FilterChain;
import http.exception.MethodNotMatchException;
import http.exception.ResourceNotFoundException;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.StatusLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Filter
public class BootstrapFilter implements BaseFilter {
    Logger logger =  LoggerFactory.getLogger(BootstrapFilter.class);
    @Override
    public void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) {
        logger.info("BootstrapFilter");

        if (request.getHeaders() != null) {
            String encoding = request.getHeaders().get("Accept-Encoding");
            String connection = request.getHeaders().get("Connection");
            if (encoding != null && encoding.contains("gzip")) {
                response.getHeaders().put("Content-Encoding", "gzip");
            }
            if (connection != null && connection.contains("close")) {
                response.getHeaders().put("Connection", "close");
            }
        }
        response.setStatusLine(new StatusLine("HTTP/1.1", 200, "OK"));
       try {
        chain.doChain(request, response);
       }catch (MethodNotMatchException ex) {
           response.setStatusLine(new StatusLine("HTTP/1.1", 405, "Method Not Allowed"));
       } catch (ResourceNotFoundException ex) {
           response.setStatusLine(new StatusLine("HTTP/1.1", 404, "Not Found"));
       } catch (Exception ex) {
           response.setStatusLine(new StatusLine("HTTP/1.1", 500, "Internal Server Error"));
       }

        if (response.getStatusLine().getStatusMessage() == null
                || response.getStatusLine().getStatusMessage().isBlank()) {
            response.getStatusLine().setStatusMessage(defaultReason(response.getStatusLine().getStatusCode()));
        }
    }
    private static String defaultReason(int code) {
        return switch (code) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 204 -> "No Content";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            case 415 -> "Unsupported Media Type";
            case 500 -> "Internal Server Error";
            default -> "";
        };
    }
}
