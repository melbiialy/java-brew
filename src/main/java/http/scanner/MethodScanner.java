package http.scanner;

import java.util.List;

public interface MethodScanner {
    void scan(List<Class<?>> classes) throws InstantiationException, IllegalAccessException;
}
