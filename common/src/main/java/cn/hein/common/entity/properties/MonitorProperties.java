package cn.hein.common.entity.properties;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Configuration properties class for monitor.
 *
 * @author hein
 */
@Data
public class MonitorProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 5967131986902182337L;

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
