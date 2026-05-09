package http.scanner;

import http.annotation.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FilterScanner extends BaseScanner{

    @Override
    public boolean matches(Class<?> clazz) {
        return clazz.isAnnotationPresent(Filter.class);
    }
}
