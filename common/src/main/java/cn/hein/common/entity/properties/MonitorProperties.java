package cn.hein.common.entity.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.concurrent.TimeUnit;

/**
 * Configuration properties class for monitor.
 *
 * @author hein
 */
@Data
@EqualsAndHashCode
public class MonitorProperties {

    /**
     * Whether the monitor is enabled.
     */
    private boolean enabled = true;

    /**
     * Interval for monitor metric data.
     */
    private int interval;

    /**
     * Time unit for monitor metric data.
     */
    private TimeUnit timeUnit;
}
