package cn.hein.common.entity.alarm;

import cn.hein.common.enums.alarm.AlarmTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Represents the content of an alarm notification.
 *
 * @author Hein
 */
@Data
public class AlarmContent {

    /**
     * The name of the thread pool that triggered the alarm.
     */
    private String threadPoolName;

    /**
     * A list of alarm items, each describing a specific alarm condition.
     */
    private List<AlarmItem> alarmItems;

    /**
     * Represents an individual item within an alarm content.
     */
    @Data
    @Builder
    public static class AlarmItem {

        /**
         * The type of alarm.
         */
        private AlarmTypeEnum type;

        /**
         * The threshold value that triggers the alarm.
         */
        private double threshold;

        /**
         * The current value being monitored.
         */
        private double value;
    }
}