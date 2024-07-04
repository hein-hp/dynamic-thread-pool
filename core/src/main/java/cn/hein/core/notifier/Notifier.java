package cn.hein.core.notifier;

import cn.hein.common.entity.alarm.AlarmContent;

/**
 * Defines the contract for a notifier.
 */
public interface Notifier {

    /**
     * Sends a notification based on the provided alarm content and timestamp.
     *
     * @param content   the content of the alarm to be notified
     * @param timestamp the timestamp at which the notification should be sent
     */
    void notify(AlarmContent content, long timestamp);
}