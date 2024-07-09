package cn.hein.core.executor;

import cn.hein.common.enums.executors.RejectionPolicyTypeEnum;
import cn.hein.core.executor.runnable.TimeoutTaskManager;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static cn.hein.core.spring.ApplicationContextHolder.getBean;

/**
 * A dynamically configurable ThreadPoolExecutor.
 *
 * @author hein
 */
@Getter
public class DynamicTpExecutor extends ThreadPoolExecutor {

    private final String threadPoolName;

    @Setter
    private long keepAliveTime;

    @Setter
    private TimeUnit unit;

    /**
     * Origin RejectionPolicy Type
     */
    @Setter
    private RejectionPolicyTypeEnum originalHandlerType;

    /**
     * The execute timeout
     */
    @Setter
    private long executeTimeOut;

    /**
     * Total Rejected Count
     */
    private final AtomicLong rejectedCount = new AtomicLong(0L);

    /**
     * Total Runnable TimeOut Count
     */
    private final AtomicLong timeoutCount = new AtomicLong(0L);

    public DynamicTpExecutor(
            String threadPoolName,
            String executorNamePrefix,
            int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            RejectedExecutionHandler handler,
            RejectionPolicyTypeEnum originalHandlerType,
            long executeTimeOut) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new NamedThreadFactory(executorNamePrefix, 0L), handler);
        this.threadPoolName = threadPoolName;
        this.keepAliveTime = keepAliveTime;
        this.unit = unit;
        this.originalHandlerType = originalHandlerType;
        this.executeTimeOut = executeTimeOut;
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        getBean(TimeoutTaskManager.class).startTimeOut(this, t, r);
    }
}