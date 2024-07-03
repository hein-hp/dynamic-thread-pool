package cn.hein.common.enums.alarm;

import lombok.Getter;

/**
 * Enum representing different types of alarms.
 *
 * @author Hein
 */
@Getter // Lombok annotation to generate getters for fields
public enum AlarmTypeEnum {

    /**
     * Represents an alarm indicating the liveness of a thread pool.
     */
    LIVENESS("liveness", "线程池活跃性告警"),

    /**
     * Represents an alarm indicating the usage rate of a blocking queue.
     */
    CAPACITY("capacity", "阻塞队列容量告警");

    /**
     * The unique code associated with the alarm type.
     */
    private final String code;

    /**
     * A descriptive text for the alarm type.
     */
    private final String desc;

    AlarmTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Finds the alarm type that corresponds to the given code.
     *
     * @param code The code to find the alarm type for.
     * @return The corresponding AlarmTypeEnum.
     * @throws IllegalArgumentException If no matching alarm type is found for the given code.
     */
    public static AlarmTypeEnum from(String code) {
        for (AlarmTypeEnum alarmType : values()) {
            if (alarmType.getCode().equals(code)) {
                return alarmType;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}