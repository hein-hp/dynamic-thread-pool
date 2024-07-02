package cn.hein.core.monitor;

import cn.hein.common.entity.monitor.ExecutorStats;
import cn.hein.common.entity.properties.DynamicTpProperties;
import cn.hein.common.pattern.chain.Filter;
import cn.hein.common.pattern.chain.FilterContext;
import cn.hein.common.pattern.chain.HandlerChain;
import cn.hein.common.pattern.chain.HandlerChainFactory;
import cn.hein.common.spring.ApplicationContextHolder;
import cn.hein.common.toolkit.StringUtil;
import cn.hein.core.DynamicTpContext;
import cn.hein.core.executor.DynamicTpExecutor;
import cn.hein.core.executor.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Abstract base class for dynamic thread pool monitors.
 *
 * @author hein
 */
@Slf4j
public abstract class AbstractMonitor implements Monitor {

    private static final ScheduledExecutorService MONITOR_EXECUTOR = new ScheduledThreadPoolExecutor(1,
            new NamedThreadFactory("dynamic-tp-monitor", 0L));
    private ScheduledFuture<?> scheduledFuture;
    private volatile boolean isRunning = false;

    @Override
    @SuppressWarnings("unchecked")
    public void collect() {
        // executor is running and enabled monitor
        if (isRunning) {
            DynamicTpContext.listDynamicTp().forEach(each -> {
                DynamicTpExecutor executor = DynamicTpContext.getDynamicTp(each);
                if (!ApplicationContextHolder.containsBean(StringUtil.kebabCaseToCamelCase(executor.getThreadPoolName()))) {
                    throw new RuntimeException("DynamicTp bean not found: " + executor.getThreadPoolName());
                }
                ExecutorStats stats = new ExecutorStats();
                stats.setThreadPoolName(each);
                HandlerChain<ExecutorStats> chain = HandlerChainFactory.buildChain(
                        context -> {
                            // TODO handle ExecutorStats
                            System.out.println("context = " + context);
                        },
                        FilterContext.getFilters("COLLECT").toArray(new Filter[0]));
                chain.execute(stats);
            });
        }
    }

    @Override
    public void monitor() {
        // TODO monitor
    }

    @Override
    public void start(DynamicTpProperties prop) {
        synchronized (this) {
            if (!isRunning) {
                isRunning = true;
                scheduledFuture = MONITOR_EXECUTOR.scheduleAtFixedRate(this::collect, 0,
                        prop.getMonitor().getInterval(), prop.getMonitor().getTimeUnit());
                log.info("MONITOR_EXECUTOR start");
            }
        }
    }

    @Override
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
     */
    protected abstract void doCollect(ExecutorStats stats, DynamicTpExecutor executor);
}