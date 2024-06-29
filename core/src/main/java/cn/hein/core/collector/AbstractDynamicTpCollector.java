package cn.hein.core.collector;

import cn.hein.common.entity.info.DynamicTpCollectInfo;
import cn.hein.common.spring.ApplicationContextHolder;
import cn.hein.common.toolkit.TimeUnitConvertUtil;
import cn.hein.core.executor.DynamicTpExecutor;
import cn.hein.core.properties.DynamicTpProperties;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Abstract base class for dynamic thread pool collectors.
 * Handles scheduling of collection tasks and interaction with registered executors.
 *
 * @author hein
 */
public abstract class AbstractDynamicTpCollector implements DynamicTpCollector {

    private static final ScheduledExecutorService COLLECTOR_EXECUTOR =
            new ScheduledThreadPoolExecutor(1,
                    new DynamicTpExecutor.NamedThreadFactory("dynamic-tp-collector", 0L));
    private ScheduledFuture<?> scheduledFuture;
    private volatile boolean isRunning = false;

    public void collect() {
        if (isRunning) {
            getRegisteredExecutors()
                    .forEach((beanName, executor) -> publish(doCollect(beanName, executor)));
        }
    }

    public void start(DynamicTpProperties properties) {
        synchronized (this) {
            if (!isRunning) {
                isRunning = true;
                scheduledFuture = COLLECTOR_EXECUTOR.scheduleAtFixedRate(this::collect, 0,
                        properties.getMonitor().getInterval(),
                        TimeUnitConvertUtil.convert(properties.getMonitor().getTimeUnit()));
            }
        }
    }

    public void stop() {
        synchronized (this) {
            if (scheduledFuture != null) {
                scheduledFuture.cancel(false); // false means do not interrupt if running
            }
            isRunning = false;
        }
    }

    /**
     * Performs the actual collection for each executor.
     *
     * @param beanName The name of the bean associated with the executor.
     * @param executor The executor to collect metrics from.
     * @return A DynamicTpCollectInfo object containing the collected metrics.
     */
    protected abstract DynamicTpCollectInfo doCollect(String beanName, DynamicTpExecutor executor);

    /**
     * Publish the collected metrics.
     *
     * @param collectInfo The DynamicTpCollectInfo object containing the collected metrics.
     */
    protected abstract void publish(DynamicTpCollectInfo collectInfo);

    /**
     * Retrieves a map of all registered DynamicTpExecutor beans.
     *
     * @return A map where keys are bean names and values are DynamicTpExecutor instances.
     */
    private Map<String, DynamicTpExecutor> getRegisteredExecutors() {
        return ApplicationContextHolder.getBeansOfType(DynamicTpExecutor.class);
    }
}