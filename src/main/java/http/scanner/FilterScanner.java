package http.scanner;

import http.annotation.Filter;
import http.routing.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FilterScanner extends BaseScanner{

    private static final Logger log = LoggerFactory.getLogger(FilterScanner.class);

    @Override
    public boolean matches(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Filter.class)){
            log.info("Filter found: {}",clazz.getName());
        }
        return clazz.isAnnotationPresent(Filter.class);
    }
}
