package cn.hein.core.monitor;

import cn.hein.common.entity.properties.DynamicTpProperties;

/**
 * Defines the contract for a dynamic thread pool monitor.
 *
 * @author hein
 */
public interface Monitor {

    /**
     * Monitors the metrics for the thread pool.
     */
    void monitor();

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