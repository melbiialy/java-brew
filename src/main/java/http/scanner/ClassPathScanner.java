package http.scanner;

import java.util.List;

public interface ClassPathScanner {
    List<Class<?>> scan(String basePackage);
    List<Class<?>> scan();

}
