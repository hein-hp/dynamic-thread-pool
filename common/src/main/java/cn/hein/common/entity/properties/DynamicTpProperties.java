package cn.hein.common.entity.properties;

import lombok.Data;

import java.util.List;

/**
 * Configuration properties class for Dynamic Thread Pools.
 *
 * @author hein
 */
@Data
public class DynamicTpProperties {

    /**
     * Whether to enable dynamic thread pools.
     */
    private boolean enabled = true;

    /**
     * Whether to notify configuration changes
     */
    private boolean change = true;

    /**
     * Monitoring configurations.
     */
    private MonitorProperties monitor;

    /**
     * Alarm platform configurations.
     */
    private List<PlatformProperties> platforms;

    /**
     * Configurations for individual executors within dynamic thread pools.
     */
    private List<ExecutorProperties> executors;

    public static DynamicTpProperties getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DynamicTpProperties INSTANCE = new DynamicTpProperties();
    }
}
