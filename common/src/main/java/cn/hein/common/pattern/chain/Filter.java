package cn.hein.common.pattern.chain;

/**
 * Defines a Filter interface that can be used in a chain of responsibility pattern.
 * Filters can intercept requests and perform some action before passing the request
 * to the next handler in the chain.
 *
 * @param <T> the type of the context object passed between filters and handlers
 * @author hein
 */
public interface Filter<T> {

    /**
     * Gets the order of the filter in the chain.
     * Filters with lower order values will be processed earlier in the chain.
     *
     * @return the order of the filter
     */
    int getOrder();

    /**
     * Gets the name of the filter.
     *
     * @return the name of the filter
     */
    String getName();

    /**
     * Performs the filtering logic.
     * This method is called when a request arrives at the filter.
     * It can optionally modify the context and must call the next handler in the chain.
     *
     * @param context the context object containing the request data
     * @param next    the next handler in the chain to pass the request to
     */
    void doFilter(T context, Handler<T> next);
}