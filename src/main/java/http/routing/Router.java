package http.routing;


import http.exception.MethodNotMatchException;
import http.exception.ResourceNotFoundException;
import http.exception.UnSupportedContentType;
import http.request.HttpRequest;
import http.routing.endpoint.definition.Endpoint;
import http.routing.endpoint.registry.EndPointRegistry;
import http.routing.endpoint.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




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
