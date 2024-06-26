package cn.hein.core.properties;

import lombok.Data;

import java.util.List;

/**
 * 动态线程池参数配置类
 *
 * @author hein
 */
@Data
public class ExecutorProperties {

    /**
     * 线程池名称
     */
    private String threadPoolName;

    /**
     * 核心线程数
     */
    private int corePoolSize;

    /**
     * 最大线程数
     */
    private int maximumPoolSize;

    /**
     * 阻塞队列类型
     */
    private String queueType;

    /**
     * 阻塞队列容量
     */
    private int queueCapacity;

    /**
     * 线程空闲时间
     */
    private int keepAliveSeconds;

    /**
     * 时间单位
     */
    private String timeUnit;

    /**
     * 是否允许核心线程超时
     */
    private boolean allowCoreThreadTimeout;

    /**
     * 拒绝策略
     */
    private String rejectedExecutionHandler;

    /**
     * 线程名称前缀
     */
    private String executorNamePrefix;

    /**
     * 是否开启告警
     */
    private boolean notifyEnable;

    /**
     * 告警平台
     */
    private String notifyPlatform;

    /**
     * 告警配置
     */
    private List<NotifyItemProperties> notifyItem;
}
