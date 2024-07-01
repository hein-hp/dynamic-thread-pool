package cn.hein.common.entity.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.concurrent.TimeUnit;

/**
 * Configuration properties class for alarm platforms.
 *
 * @author hein
 */
@Data
@EqualsAndHashCode
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
    private TimeUnit notifyTimeUnit;
}
