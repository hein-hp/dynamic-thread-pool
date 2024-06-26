package cn.hein.core.collector;

import cn.hein.core.properties.DynamicTpProperties;

/**
 * Defines the contract for a dynamic thread pool collector.
 * Implementations of this interface are responsible for collecting metrics related to thread pools,
 * starting and stopping the collection process.
 *
 * @author hein
 */
public interface DynamicTpCollector {

    /**
     * Collects metrics from the thread pool.
     * This method should be implemented to perform the actual collection logic.
     *
     * @param properties The properties required to configure the collection process.
     */
    void collect(DynamicTpProperties properties);

    /**
     * Starts the collection process.
     *
     * @param properties The properties required to configure the collection process.
     */
    void start(DynamicTpProperties properties);

    /**
     * Stops the collection process.
     * This method should halt any ongoing collection activities.
     */
    void stop();
}