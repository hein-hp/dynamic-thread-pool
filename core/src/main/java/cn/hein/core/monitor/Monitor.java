package cn.hein.core.monitor;

import cn.hein.common.entity.monitor.ExecutorStats;
import cn.hein.common.entity.properties.DynamicTpProperties;
import cn.hein.common.entity.properties.ExecutorProperties;

/**
 * Defines the contract for a dynamic thread pool monitor.
 *
 * @author hein
 */
public interface Monitor {

    /**
     * Monitors the metrics for the thread pool.
     */
    void monitor(ExecutorStats stats, ExecutorProperties prop);

    /**
     * Collects the metrics for the thread pool.
     */
    void collect();

    /**
     * Starts the monitor process.
     */
    void start(DynamicTpProperties prop);

    /**
     * Stops the monitor process.
     */
    void stop();
}