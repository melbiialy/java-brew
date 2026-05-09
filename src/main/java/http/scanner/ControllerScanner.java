package http.scanner;

import http.annotation.Controller;


public class ControllerScanner extends BaseScanner{
    private volatile static ControllerScanner instance;
    public static ControllerScanner getInstance() {
        if (instance == null) {
            synchronized (ControllerScanner.class) {
                if (instance == null) {
                    instance = new ControllerScanner();
                }
            }
        }
        return instance;
    }
    @Override
    public boolean matches(Class<?> clazz) {
        return clazz.isAnnotationPresent(Controller.class);
    }

}
