package cn.hein.common.enums.alarm;

import lombok.Getter;

/**
 * @author hein
 */
@Getter
public enum NotifyTypeEnum {

    /**
     * Email Notify
     */
    EMAIL("email");

    /**
     * A descriptive text for the alarm type.
     */
    private final String desc;

    NotifyTypeEnum(String desc) {
        this.desc = desc;
    }

    /**
     * Finds the notify type that corresponds to the given desc.
     *
     * @param desc The desc to find the notify type for.
     * @return The corresponding AlarmTypeEnum.
     * @throws IllegalArgumentException If no matching notify type is found for the given desc.
     */
    public static NotifyTypeEnum from(String desc) {
        for (NotifyTypeEnum typeEnum : values()) {
            if (typeEnum.getDesc().equals(desc)) {
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("Invalid desc: " + desc);
    }
}
