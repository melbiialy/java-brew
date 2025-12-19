package http.annotation;

import http.enums.HttpMethod;

import java.lang.annotation.*;

/**
 * Annotation used to define an HTTP endpoint in a web application.
 * This annotation is applied to methods and allows specifying
 * the URL path and the HTTP method for the endpoint.
 *
 * The `path` element represents the URL mapping for the endpoint and
 * defaults to the root path ("/") if no value is specified.
 *
 * The `method` element specifies the HTTP method (e.g., GET, POST, PUT, DELETE)
 * that the endpoint responds to. The default value is `HttpMethod.GET`.
 *
 * This annotation is typically used in combination with routing mechanisms
 * to map incoming HTTP requests to the appropriate method in a controller.
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EndPoint {
    String path() default "/";
    HttpMethod method() default HttpMethod.GET;
}
