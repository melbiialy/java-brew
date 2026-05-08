package http.routing.endpoint.definition;

import http.enums.HttpMethod;
import http.routing.endpoint.path.PathPattern;

public record EndPointInfo(HttpMethod method, PathPattern pattern, String  consumes, String produces) {
}
