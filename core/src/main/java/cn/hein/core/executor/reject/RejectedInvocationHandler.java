package cn.hein.core.executor.reject;

import cn.hein.core.executor.DynamicTpExecutor;
import cn.hein.core.monitor.RejectedMonitor;
import cn.hein.core.spring.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;

/**
 * A dynamic proxy handler for RejectedExecutionHandler.
 *
 * @author hein
 */
@Slf4j
public class RejectedInvocationHandler implements InvocationHandler {

    /**
     * The target RejectedExecutionHandler that this handler wraps around.
     */
    private final RejectedExecutionHandler target;

    public RejectedInvocationHandler(RejectedExecutionHandler target) {
        this.target = target;
    }

    public RejectedExecutionHandler getTarget() {
        return target;
    }

    /**
     * Invokes methods on the target RejectedExecutionHandler.
     * Before invoking the method on the target, it calls beforeReject().
     * After the invocation, it calls afterReject() regardless of whether an exception was thrown.
     *
     * @param proxy  the proxy instance that the method was invoked on
     * @param method the method that was invoked
     * @param args   the arguments passed to the method
     * @return the result of the method invocation on the target
     * @throws Throwable any throwable thrown by the method invocation on the target
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        beforeReject((Runnable) args[0], (Executor) args[1]);
        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException ex) {
            throw ex.getCause(); // Rethrow the cause of the InvocationTargetException
        } finally {
            afterReject((Runnable) args[0], (Executor) args[1]);
        }
    }

    /**
     * Performs actions before the rejection is handled by the target RejectedExecutionHandler.
     *
     * @param runnable the task that was rejected
     * @param executor the executor that attempted to execute the task
     */
    private void beforeReject(Runnable runnable, Executor executor) {
        // inc rejected count
        log.info("Rejected task: {}", runnable);
        RejectedMonitor monitor = ApplicationContextHolder.getBean(RejectedMonitor.class);
        if (executor instanceof DynamicTpExecutor dynamicTpExecutor) {
            monitor.incCycleRejectedCount(dynamicTpExecutor);
        }
    }

    /**
     * Performs actions after the rejection has been handled by the target RejectedExecutionHandler.
     *
     * @param runnable the task that was rejected
     * @param executor the executor that attempted to execute the task
     */
    private void afterReject(Runnable runnable, Executor executor) {
        // no option
    }
}