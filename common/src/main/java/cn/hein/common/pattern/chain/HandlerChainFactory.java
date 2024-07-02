package cn.hein.common.pattern.chain;

/**
 * Factory class for creating a HandlerChain with a series of filters.
 *
 * @author hein
 */
public final class HandlerChainFactory {

    private HandlerChainFactory() {}

    /**
     * Builds a HandlerChain by wrapping a target handler with a series of filters.
     *
     * @param target  The target handler that will receive the request after all filters have been applied.
     * @param filters An array of filters to apply to the chain. Filters are applied in reverse order of appearance.
     * @param <T>     The type of the context object that will be passed through the chain.
     * @return A new HandlerChain instance with the target handler wrapped in the provided filters.
     */
    @SafeVarargs
    public static <T> HandlerChain<T> buildChain(Handler<T> target, Filter<T>... filters) {
        HandlerChain<T> chain = new HandlerChain<>();
        Handler<T> last = target;
        for (int i = filters.length - 1; i >= 0; i--) {
            Handler<T> next = last;
            Filter<T> filter = filters[i];
            last = context -> filter.doFilter(context, next);
        }
        chain.setHead(last);
        return chain;
    }
}