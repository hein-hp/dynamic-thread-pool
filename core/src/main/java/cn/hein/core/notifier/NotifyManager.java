package cn.hein.core.notifier;

import cn.hein.common.entity.alarm.AlarmContent;
import cn.hein.common.entity.properties.DynamicTpProperties;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Manages notification logic for different thread pools.
 *
 * @author hein
 */
public class NotifyManager {

    private static final Map<String, ExecutorNotifyManager> managers = new ConcurrentHashMap<>();

    private static final DynamicTpProperties properties = DynamicTpProperties.getInstance();

    private static final Lock lock = new ReentrantLock();

    /**
     * Represents the notification manager for an executor.
     */
    @Data
    @Builder
    private static class ExecutorNotifyManager {

        private String threadPoolName;

        private long lastNotifyTime = Long.MIN_VALUE; // Ensure the first notification always sent

        private int notifyInterval;

        private TimeUnit unit;

        /**
         * Checks if it's time to send a notification based on the interval.
         *
         * @param timestamp Current system time in milliseconds.
         * @return true if it's time to send a notification, false otherwise.
         */
        public boolean canNotify(long timestamp) {
            if (timestamp - unit.toMillis(notifyInterval) >= lastNotifyTime) {
                lastNotifyTime = timestamp;
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Determines if a notification can be sent for the given alarm content at the current timestamp.
     *
     * @param content   Alarm content including thread pool name.
     * @param timestamp Current system time in milliseconds.
     * @return true if a notification can be sent, false otherwise.
     */
    public static boolean canNotify(AlarmContent content, long timestamp) {
        lock.lock();
        try {
            return Optional.ofNullable(managers.get(content.getThreadPoolName()))
                    .orElseThrow(() -> new RuntimeException("no ExecutorNotifyManager found.")).canNotify(timestamp);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Refreshes the notification managers by clearing and reloading them from properties.
     */
    public static void refresh() {
        lock.lock();
        try {
            clear();
            load();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Loads notification managers from properties.
     */
    private static void load() {
        properties.getExecutors().forEach(prop -> {
            String threadPoolName = prop.getThreadPoolName();
            ExecutorNotifyManager manager = ExecutorNotifyManager.builder()
                    .threadPoolName(threadPoolName)
                    .notifyInterval(prop.getNotify().getNotifyInterval())
                    .unit(prop.getNotify().getNotifyTimeUnit())
                    .build();
            managers.putIfAbsent(threadPoolName, manager);
        });
    }

    /**
     * Clears all notification managers.
     */
    private static void clear() {
        managers.clear();
    }
}