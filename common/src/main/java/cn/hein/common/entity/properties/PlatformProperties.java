package cn.hein.common.entity.properties;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Configuration properties class for notify platforms.
 *
 * @author hein
 */
@Data
public class PlatformProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = -6138554731317376011L;

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
