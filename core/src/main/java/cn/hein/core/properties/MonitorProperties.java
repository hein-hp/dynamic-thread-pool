package cn.hein.core.properties;

import lombok.Data;

/**
 * Configuration properties class for monitor.
 *
 * @author hein
 */
@Data
public class MonitorProperties {

    /**
     * Whether the monitor is enabled.
     */
    private boolean enabled;

    /**
     * Interval for monitor metric data.
     */
    private int interval;

    /**
     * Time unit for monitor metric data.
     */
    private String timeUnit;
}
