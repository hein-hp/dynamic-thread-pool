package cn.hein.common.entity.info;

import lombok.Builder;
import lombok.Data;

/**
 * This class represents the collected information about a dynamic thread pool.
 * It encapsulates various metrics and properties of the thread pool for monitoring purposes.
 *
 * @author hein
 */
@Data
@Builder
public class DynamicTpCollectInfo {

    /**
     * The name of the bean associated with the thread pool.
     */
    private String beanName;

    /**
     * The current number of threads in the thread pool.
     */
    private int poolSize;

    /**
     * The core number of threads in the thread pool.
     */
    private int corePoolSize;

    /**
     * The maximum number of threads that the thread pool will allow.
     */
    private int maximumPoolSize;

    /**
     * The number of threads currently executing tasks.
     */
    private int activeCount;

    /**
     * The total number of tasks that have been submitted to the thread pool.
     */
    private long taskCount;

    /**
     * The total number of tasks that have been completed by the thread pool.
     */
    private long completedTaskCount;

    /**
     * The largest number of threads that have ever simultaneously been active in the thread pool.
     */
    private int largestPoolSize;

    /**
     * The type of work queue used by the thread pool (e.g., ArrayBlockingQueue, LinkedBlockingQueue).
     */
    private String queueType;

    /**
     * The capacity of the work queue.
     */
    private int queueCapacity;

    /**
     * The number of elements that can still be added to the work queue.
     */
    private int remainingCapacity;

    /**
     * The type of rejection handler used by the thread pool when the queue is full.
     */
    private String handlerType;
}