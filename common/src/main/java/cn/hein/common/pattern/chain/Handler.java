package cn.hein.common.pattern.chain;

/**
 * Defines a handler interface that is used in the Chain of Responsibility pattern.
 *
 * @param <T> The type of the context object that will be handled by this handler.
 * @author hein
 */
public interface Handler<T> {

    /**
     * Handles the given context object.
     *
     * @param context The context object to be handled.
     */
    void handle(T context);
}