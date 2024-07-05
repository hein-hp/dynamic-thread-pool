package cn.hein.common.entity.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Configuration properties class for notify.
 *
 * @author hein
 */
@Data
@EqualsAndHashCode
public class NotifyProperties {

    /**
     * Whether to enable alert notifications.
     */
    private boolean enabled = true;

    /**
     * Notification platform configuration.
     */
    private String platformKey;

    /**
     * Notify notification interval.
     */
    private int notifyInterval;

    /**
     * Notify notification interval unit.
     */
    private TimeUnit notifyTimeUnit;

    /**
     * Notification item configuration.
     */
    private List<NotifyItem> notifyItem;

    /**
     * Configuration properties class for monitoring notification items.
     *
     * @author hein
     */
    @Data
    @EqualsAndHashCode
    public static class NotifyItem {

        /**
         * Monitoring type.
         */
        private String type;

        /**
         * Whether the notification item is enabled.
         */
        private boolean enabled = true;

        /**
         * Threshold value for triggering the notification.
         */
        private double threshold;
    }
}
