package http.context;

import http.scanner.ClassPathScanner;

import java.util.List;

public class ListableControllerContext implements ControllerContext{
    private List<Class<?>> controllers;
    private ClassPathScanner scanner;
    public ListableControllerContext(ClassPathScanner scanner) {
        this.scanner = scanner;
        controllers = scanner.scan("");
    }

    public List<Class<?>> getControllers() {
        return controllers;
    }
    public void setControllers(List<Class<?>> controllers) {
        this.controllers = controllers;
    }
    public void addController(Class<?> controller) {
        controllers.add(controller);
    }
    public void removeController(Class<?> controller) {
        controllers.remove(controller);
    }
    public void clearControllers() {
        controllers.clear();
    }
}
