package cn.hein.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration properties class for Dynamic Thread Pools.
 *
 * @author hein
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "dynamic.thread-pool")
public class DynamicTpProperties {

    /**
     * Whether to enable dynamic thread pools.
     */
    private boolean enable;

    /**
     * Whether to notify configuration changes
     */
    private boolean change;

    /**
     * Whether to enable monitor.
     */
    private boolean monitorEnable;

    /**
     * Interval for monitor metric data.
     */
    private int monitorInterval;

    /**
     * Time unit for monitor metric data.
     */
    private String monitorTimeUnit;

    /**
     * Alarm platform configurations.
     */
    private List<PlatformProperties> platforms;

    /**
     * Configurations for individual executors within dynamic thread pools.
     */
    private List<ExecutorProperties> executors;
}
