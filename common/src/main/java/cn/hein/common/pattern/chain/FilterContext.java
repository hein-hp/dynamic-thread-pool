package cn.hein.common.pattern.chain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A utility class that manages a collection of filters within a filter chain.
 */
@SuppressWarnings("rawtypes")
public final class FilterContext {

    // A concurrent map that holds lists of filters associated with specific names.
    private static final Map<String, List<Filter>> FILTER_CONTEXT = new ConcurrentHashMap<>(16);

    /**
     * Registers a new filter with a specified name.
     *
     * @param name   The name under which the filter should be registered.
     * @param filter The filter to register.
     */
    public synchronized static void registerFilter(String name, Filter filter) {
        List<Filter> filters = FILTER_CONTEXT.computeIfAbsent(name, k -> new ArrayList<>());
        filters.add(filter);
    }

    /**
     * Retrieves and sorts the filters associated with the specified name by their order.
     *
     * @param name The name of the filters to retrieve.
     * @return A sorted list of filters.
     */
    public static List<Filter> getFilters(String name) {
        // Sorts the filters by their order before returning them.
        List<Filter> filters = FILTER_CONTEXT.get(name);
        if (filters != null) {
            filters.sort(Comparator.comparingInt(Filter::getOrder));
        }
        return filters;
    }
}