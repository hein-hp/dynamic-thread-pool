package cn.hein.core.executor.reject;

import cn.hein.common.enums.executors.RejectionPolicyTypeEnum;

import java.lang.reflect.Proxy;
import java.util.concurrent.RejectedExecutionHandler;

/**
 * Factory for creating proxy instances of RejectedExecutionHandler.
 *
 * @author hein
 */
public class RejectHandlerProxyFactory {

    /**
     * Creates a proxy for the given rejection policy type.
     *
     * @param type the type of rejection policy to create a handler for
     * @return a proxy instance of RejectedExecutionHandler
     */
    public static RejectedExecutionHandler getProxy(RejectionPolicyTypeEnum type) {
        // Retrieve the handler associated with the given policy type
        RejectedExecutionHandler handler = type.getHandler();
        return getProxy(handler);
    }

    /**
     * Creates a proxy for the given RejectedExecutionHandler.
     *
     * @param handler the handler to create a proxy for
     * @return a proxy instance of RejectedExecutionHandler
     */
    private static RejectedExecutionHandler getProxy(RejectedExecutionHandler handler) {
        // Create a new proxy instance using the handler's class loader and interfaces
        // Wrap the handler with a RejectedInvocationHandler to add additional behavior
        return (RejectedExecutionHandler) Proxy.newProxyInstance(
                handler.getClass().getClassLoader(),
                new Class<?>[]{RejectedExecutionHandler.class},
                new RejectedInvocationHandler(handler));
    }
}