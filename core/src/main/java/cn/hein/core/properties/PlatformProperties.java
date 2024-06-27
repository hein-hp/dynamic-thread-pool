package cn.hein.core.properties;

import lombok.Data;

/**
 * Configuration properties class for alarm platforms.
 *
 * @author hein
 */
@Data
public class PlatformProperties {

    /**
     * Alarm notification platform (currently supports email only).
     */
    private String platform;

    /**
     * Recipient's email address.
     */
    private String receiveEmail;
}
