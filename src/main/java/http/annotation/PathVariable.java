package http.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to bind a method parameter to a path variable in a URL.
 * Path variables are specified within curly braces in the path definition
 * of an endpoint, and their values are extracted from the URL at runtime to
 * populate the corresponding method parameter annotated with this annotation.
 *
 * This annotation is typically used in the context of routing and HTTP request
 * handling to dynamically map parts of a URL to method arguments.
 */
@Target(value = ElementType.PARAMETER)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface PathVariable {
    String value();
}
