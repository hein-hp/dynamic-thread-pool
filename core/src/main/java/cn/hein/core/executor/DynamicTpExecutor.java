package cn.hein.core.executor;

import lombok.Getter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A dynamically configurable ThreadPoolExecutor.
 *
 * @author hein
 */
@Getter
public class DynamicTpExecutor extends ThreadPoolExecutor {

    private final String threadPoolName;

    public DynamicTpExecutor(
            String threadPoolName,
            String executorNamePrefix,
            int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new NamedThreadFactory(executorNamePrefix, 0L), handler);
        this.threadPoolName = threadPoolName;
    }
}