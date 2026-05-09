package http.context;

import http.bootstrap.BootstrapFilter;
import http.scanner.ClassPathScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class IterableFilterContext implements FilterContext {
    private static final Logger log = LoggerFactory.getLogger(IterableFilterContext.class);
    List<BaseFilter> baseFilters = new ArrayList<>();
   public ThreadLocal<Integer> filterIndex = ThreadLocal.withInitial(() -> 0);
    ClassPathScanner scanner;
    public IterableFilterContext(ClassPathScanner scanner){
        this.scanner = scanner;
        baseFilters.add(new BootstrapFilter());
    }

    public List<BaseFilter> getFilters() {
        return baseFilters;
    }
    public void refresh(){
        refresh(scanner.scan());
    }

    public void refresh(List<Class<?>> filters) {
        filters.stream()
                .filter(this::isFilter)
                .map(this::instantiate)
                .map(BaseFilter.class::cast)
                .forEach(this.baseFilters::add);
    }

    private boolean isFilter(Class<?> clazz) {
        if (!BaseFilter.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(
                    clazz.getName() + " does not implement Filter"
            );
        }
        return true;
    }

    private Object instantiate(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate filter: " + clazz.getName(), e);
        }
    }

    public BaseFilter getNextFilter() {
        if (filterIndex.get() == baseFilters.size()) {
            return null;
        }
        BaseFilter baseFilter = baseFilters.get(filterIndex.get());
        filterIndex.set(filterIndex.get() + 1);
        return baseFilter;
    }

    @Override
    public void set() {
        filterIndex.set(0);
    }
}
