package http.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to define a controller in a web application.
 * This annotation is applied to classes and serves as a marker
 * for controllers that handle HTTP requests.
 *
 * The `path` element specifies the base path for the controller.
 * Routes defined in the controller will be prefixed with this path.
 * It defaults to an empty string, which represents the root path.
 *
 * This annotation is typically used in conjunction with other routing
 * mechanisms to map HTTP requests to the appropriate controller and
 * methods.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Controller {
    String path() default "";
}
