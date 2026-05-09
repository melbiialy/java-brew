package http.scanner;

import http.annotation.Controller;


public class ControllerScanner extends BaseScanner{
    @Override
    public boolean matches(Class<?> clazz) {
        return clazz.isAnnotationPresent(Controller.class);
    }

}
