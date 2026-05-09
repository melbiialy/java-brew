package http.scanner;

import http.annotation.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FilterScanner extends BaseScanner{

    private static final Logger log = LoggerFactory.getLogger(FilterScanner.class);
    private volatile static FilterScanner instance;
    public static FilterScanner getInstance() {
        if (instance == null) {
            synchronized (FilterScanner.class) {
                if (instance == null) {
                    instance = new FilterScanner();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean matches(Class<?> clazz) {
        return clazz.isAnnotationPresent(Filter.class);
    }
}
