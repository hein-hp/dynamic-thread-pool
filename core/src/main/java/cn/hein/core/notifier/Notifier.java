package cn.hein.core.notifier;

import cn.hein.common.entity.alarm.AlarmContent;

/**
 * Defines the contract for a notifier.
 *
 * @author hein
 */
public interface Notifier {

    /**
     * Sends a notification based on the provided alarm content and timestamp.
     *
     * @param content   the content of the alarm to be notified
     * @param timestamp the timestamp at which the notification should be sent
     * @param sendKey   the send key for the notification
     */
    void notify(AlarmContent content, long timestamp, String sendKey) throws Exception;
}