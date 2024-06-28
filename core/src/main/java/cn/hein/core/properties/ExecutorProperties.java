package cn.hein.core.properties;

import lombok.Data;

import java.util.List;

/**
 * Configuration properties class for individual Executor settings within dynamic thread pools.
 *
 * @author hein
 */
@Data
public class ExecutorProperties {

    /**
     * The name of the thread pool.
     */
    private String threadPoolName;

    /**
     * Core pool size of threads.
     */
    private int corePoolSize;

    /**
     * Maximum pool size of threads.
     */
    private int maximumPoolSize;

    /**
     * Type of the blocking queue.
     */
    private String queueType;

    /**
     * Capacity of the blocking queue.
     */
    private int queueCapacity;

    /**
     * Keep-alive time for idle threads.
     */
    private int keepAliveTime;

    /**
     * Time unit for the keep-alive time.
     */
    private String timeUnit;

    /**
     * Whether core threads can time out.
     */
    private boolean allowCoreThreadTimeout;

    /**
     * Rejected execution handler policy.
     */
    private String rejectedExecutionHandler;

    /**
     * Prefix for naming threads.
     */
    private String executorNamePrefix;

    /**
     * Detailed notification item configurations.
     */
    private List<NotifyItemProperties> notifyItem;

    /**
     * Whether to enable alert notifications.
     */
    private boolean notifyEnable;

    /**
     * Notification platform configuration.
     */
    private String notifyPlatform;

    /**
     * Bean name in the Spring context.
     */
    private String beanName;
}
