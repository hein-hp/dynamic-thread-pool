package cn.hein.common.entity.monitor;

import lombok.Data;

/**
 * Represents statistics for an executor service.
 *
 * @author hein
 */
@Data
public class ExecutorStats {

    /**
     * The name of the thread pool.
     */
    private String threadPoolName;

    /**
     * The number of threads currently executing tasks.
     */
    private int activeCount;

    /**
     * The maximum number of threads that can be created in the pool.
     */
    private int maximumPoolSize;

    /**
     * The current size of the task queue.
     */
    private int queueSize;

    /**
     * The remaining capacity of the task queue.
     */
    private int remainingQueueCapacity;

    /**
     * The rejected count in the cycle.
     */
    private long rejectedCycleCount;

    /**
     * The total rejected count.
     */
    private long rejectedCount;

    /**
     * The execute timeout runnable count in the cycle.
     */
    private long timeoutCycleCount;

    /**
     * The execute timeout runnable count.
     */
    private long timeoutCount;

}