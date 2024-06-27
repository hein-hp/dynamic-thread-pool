package cn.hein.core.properties;

import lombok.Data;

/**
 * Configuration properties class for monitoring notification items.
 *
 * @author hein
 */
@Data
public class NotifyItemProperties {

    /**
     * Monitoring type.
     */
    private String type;

    /**
     * Whether the notification item is enabled.
     */
    private boolean enabled;

    /**
     * Threshold value for triggering the notification.
     */
    private double threshold;
}
