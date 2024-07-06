package cn.hein.common.entity.properties;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Configuration properties class for Dynamic Thread Pools.
 *
 * @author hein
 */
@Data
public class DynamicTpProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = -2352174447056567161L;

    /**
     * Whether to enable dynamic thread pools.
     */
    private boolean enabled = true;

    /**
     * Whether to notify configuration changes
     */
    private ChangeProperties change;

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
