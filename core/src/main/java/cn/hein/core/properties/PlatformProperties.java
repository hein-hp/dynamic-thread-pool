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
     * Alarm notification platform
     */
    private String platform;

    /**
     * Alarm notification recipient.
     */
    private String receiver;

    /**
     * Alarm notification interval.
     */
    private String notifyInterval;

    /**
     * Alarm notification interval unit.
     */
    private String notifyTimeUnit;
}
