package http.routing.endpoint.definition;

import http.enums.HttpStatus;

public record ResponseDescriptor(Class<?> returnType, HttpStatus status,String contentType){
}
