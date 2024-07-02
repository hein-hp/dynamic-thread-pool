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
     * The maximum capacity of the task queue.
     */
    private int queueCapacity;
}