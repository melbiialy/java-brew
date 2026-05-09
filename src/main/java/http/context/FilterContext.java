package http.context;

import java.util.List;

public interface FilterContext {

     List<BaseFilter> getFilters();

     void setFilters(List<Class<?>> baseFilters);

     BaseFilter getNextFilter();

     void set();
}
