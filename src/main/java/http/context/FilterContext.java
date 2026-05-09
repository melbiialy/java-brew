package http.context;

import java.util.List;

public interface FilterContext {

     List<BaseFilter> getFilters();

     void refresh(List<Class<?>> baseFilters);
     void refresh();

     BaseFilter getNextFilter();

     void set();
}
