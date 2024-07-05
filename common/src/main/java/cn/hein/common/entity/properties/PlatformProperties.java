package cn.hein.common.entity.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Configuration properties class for notify platforms.
 *
 * @author hein
 */
@Data
@EqualsAndHashCode
public class PlatformProperties {

    /**
     * Notify notification key.
     */
    private String key;

    /**
     * Notify notification platform
     */
    private String platform;

    /**
     * Notify notification recipient.
     */
    private String receiver;

    /**
     * Whether to enable notification.
     */
    private boolean enabled;
}
