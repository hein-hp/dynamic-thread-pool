package cn.hein.common.entity.properties;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Configuration properties class for individual Executor settings within dynamic thread pools.
 *
 * @author hein
 */
@Data
public class ExecutorProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 1739858411226265256L;

    /**
     * The name of the thread pool.
     */
    private String threadPoolName = "default";

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
    private String queueType = "ResizeLinkedBlockingQueue";

    /**
     * Capacity of the blocking queue.
     */
    private int queueCapacity = 1024;

    /**
     * Keep-alive time for idle threads.
     */
    private int keepAliveTime = 60;

    /**
     * Time unit for the keep-alive time.
     */
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    /**
     * Whether core threads can time out.
     */
    private boolean allowCoreThreadTimeOut = false;

    /**
     * Rejected execution handler policy.
     */
    private String rejectedExecutionHandler = "Abort";

    /**
     * Prefix for naming threads.
     */
    private String executorNamePrefix = "";

    /**
     * TimeOut for runnable execute.
     */
    private long executeTimeOut;

    /**
     * Whether to enable monitoring.
     */
    private boolean monitorEnable;

    /**
     * Detailed notification configurations.
     */
    private NotifyProperties notify;

    /**
     * Bean name in the Spring context.
     */
    private String beanName;
}
