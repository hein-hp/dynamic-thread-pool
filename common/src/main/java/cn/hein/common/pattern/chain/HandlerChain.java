package cn.hein.common.pattern.chain;

import lombok.Setter;

/**
 * Represents a chain of handlers following the Chain of Responsibility design pattern.
 *
 * @param <T> The type of the context object that will be passed through the chain.
 * @author hein
 */
@Setter
public class HandlerChain<T> {

    /**
     * Reference to the head (first handler) of the chain.
     */
    private Handler<T> head;

    /**
     * Executes the chain by passing the context to the head of the chain.
     * Each handler in the chain will decide if it should handle the context,
     * or pass it to the next handler in the chain.
     *
     * @param context The context object to be processed by the chain.
     */
    public void execute(T context) {
        head.handle(context);
    }
}