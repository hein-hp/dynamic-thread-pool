package cn.hein.common.entity.properties;

import lombok.Data;

/**
 * Configuration properties class for notify when change
 *
 * @author hein
 */
@Data
public class ChangeProperties {

    /**
     * Whether to notify configuration changes
     */
    private boolean enabled = true;

    /**
     * Notification platform key.
     */
    private String platformKey;
}
