package http.context;

import java.util.List;

public interface ControllerContext {
     List<Class<?>> getControllers();
     void setControllers(List<Class<?>> controllers);
     void addController(Class<?> controller);
     void removeController(Class<?> controller);
     void clearControllers();
}
