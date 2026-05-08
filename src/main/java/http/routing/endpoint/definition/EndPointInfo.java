package http.routing.endpoint.definition;

import http.enums.ContentType;
import http.enums.HttpMethod;
import http.routing.endpoint.path.PathPattern;

public record EndPointInfo(HttpMethod method, PathPattern pattern, ContentType consumes, ContentType produces) {
}
