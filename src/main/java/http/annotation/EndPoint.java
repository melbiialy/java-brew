package http.annotation;

import http.enums.HttpMethod;

import java.lang.annotation.*;


@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EndPoint {
    String path() default "/";
    HttpMethod method() default HttpMethod.GET;
}
