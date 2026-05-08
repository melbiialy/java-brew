package http.routing;


import http.enums.HttpMethod;
import http.exception.MethodNotMatchException;
import http.exception.ResourceNotFoundException;
import http.exception.UnSupportedContentType;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.routing.endpoint.definition.Endpoint;
import http.routing.endpoint.registry.EndPointRegistry;
import http.routing.endpoint.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * A Router class responsible for mapping HTTP requests to their respective
 * endpoint definitions and executing the appropriate logic.
 * It supports registering routes dynamically and routing HTTP requests
 * based on the provided path and method.
 */
public class Router {
    private static final Logger log = LoggerFactory.getLogger(Router.class);
    private final Registry registry;

    public Router() {
        this.registry = EndPointRegistry.getInstance();
    }


    public Endpoint route(HttpRequest httpRequest)  {
        log.info("Routing request: {}", httpRequest);
        Endpoint matchedEndpoint = registry.getEndPoint(httpRequest);
        if (matchedEndpoint != null) {
            return matchedEndpoint;
        }
        if (registry.anyMatchedPathAndMethod(httpRequest.getRequestLine().getPath(),httpRequest.getRequestLine().getMethod())){
            throw new UnSupportedContentType("UnSupported Content-Type");
        }
        else if (registry.anyMatchesPath(httpRequest.getRequestLine().getPath())){
            throw new MethodNotMatchException("Method Not Match");
        }
        else{
            throw new ResourceNotFoundException("Resource Not Found");
        }

    }

}
