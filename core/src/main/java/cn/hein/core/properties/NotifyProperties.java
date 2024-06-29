package cn.hein.core.properties;

import lombok.Data;

import java.util.List;

/**
 * Configuration properties class for notify.
 *
 * @author hein
 */
@Data
public class NotifyProperties {

    /**
     * Whether to enable alert notifications.
     */
    private boolean enabled;

    /**
     * Notification platform configuration.
     */
    private String platform;

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
    public static class NotifyItem {

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
}
