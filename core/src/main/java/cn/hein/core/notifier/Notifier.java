package cn.hein.core.notifier;

import cn.hein.common.entity.alarm.AlarmContent;
import cn.hein.common.equator.FieldInfo;

import java.util.List;
import java.util.Map;

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
     * @throws Exception if an error occurs during the notification process
     */
    void alarmNotify(AlarmContent content, long timestamp, String sendKey) throws Exception;

    /**
     * Sends a notification based on the provided changed fields, timestamp, and send key.
     *
     * @param changedFields the changed fields
     * @param timestamp     the timestamp at which the notification should be sent
     * @param sendKey       the send key for the notification
     * @throws Exception if an error occurs during the notification process
     */
    void changeNotify(Map<String, List<FieldInfo>> changedFields, long timestamp, String sendKey) throws Exception;
}