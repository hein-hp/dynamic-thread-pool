package cn.hein.core.monitor;

import cn.hein.common.entity.alarm.AlarmContent;
import cn.hein.common.entity.monitor.ExecutorStats;
import cn.hein.common.entity.properties.DynamicTpProperties;
import cn.hein.common.entity.properties.ExecutorProperties;
import cn.hein.common.entity.properties.NotifyProperties;
import cn.hein.common.enums.alarm.AlarmTypeEnum;
import cn.hein.common.pattern.chain.Filter;
import cn.hein.common.pattern.chain.FilterContext;
import cn.hein.common.pattern.chain.HandlerChain;
import cn.hein.common.pattern.chain.HandlerChainFactory;
import cn.hein.core.spring.ApplicationContextHolder;
import cn.hein.core.context.DynamicTpContext;
import cn.hein.core.executor.DynamicTpExecutor;
import cn.hein.core.executor.NamedThreadFactory;
import cn.hein.core.publisher.NotifyEventPublisher;
import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
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
        try {
            if (isRunning) {
                DynamicTpProperties.getInstance().getExecutors().stream().filter(ExecutorProperties::isMonitorEnable).forEach(each -> {
                    DynamicTpExecutor executor = DynamicTpContext.getDynamicTp(each.getThreadPoolName());
                    if (!ApplicationContextHolder.containsBean(each.getBeanName())) {
                        throw new RuntimeException("DynamicTp bean not found.");
                    }
                    ExecutorStats stats = new ExecutorStats();
                    stats.setThreadPoolName(executor.getThreadPoolName());
                    HandlerChain<ExecutorStats> chain = HandlerChainFactory.buildChain(
                            // must cast
                            context -> monitor((ExecutorStats) context, each),
                            FilterContext.getFilters("COLLECT").toArray(new Filter[0]));
                    // stats will bean fill properties
                    chain.execute(stats);
                });
            }
        } catch (Exception e) {
            log.info("collect exception", e);
        }
    }

    @Override
    public void monitor(ExecutorStats stats, ExecutorProperties prop) {
        AlarmContent content = new AlarmContent();
        content.setThreadPoolName(stats.getThreadPoolName());
        prop.getNotify().getNotifyItem().forEach(each -> {
            if (each.isEnabled()) {
                doMonitor(stats, each, content);
            }
        });
        if (CollUtil.isNotEmpty(content.getAlarmItems()) && needAlarm(prop)) {
            // alarm
            NotifyEventPublisher publisher = ApplicationContextHolder.getBean(NotifyEventPublisher.class);
            publisher.publishEvent(content);
        }
    }

    private boolean needAlarm(ExecutorProperties prop) {
        return prop.getNotify().isEnabled();
    }

    private void doMonitor(ExecutorStats stats, NotifyProperties.NotifyItem item, AlarmContent content) {
        switch (AlarmTypeEnum.from(item.getType())) {
            case LIVENESS -> {
                if (item.isEnabled()) {
                    monitorLiveness(stats, item.getThreshold(), content);
                }
            }
            case CAPACITY -> {
                if (item.isEnabled()) {
                    monitorCapacity(stats, item.getThreshold(), content);
                }
            }
            case REJECTED -> {
                if (item.isEnabled()) {
                    monitorRejected(stats, item.getThreshold(), content);
                }
            }
            default -> log.warn("no monitor type.");
        }
    }

    private void monitorRejected(ExecutorStats stats, double threshold, AlarmContent content) {
        double rejected = stats.getRejectedCycleCount();
        if (rejected >= threshold) {
            if (content.getAlarmItems() == null) {
                content.setAlarmItems(new ArrayList<>());
            }
            AlarmContent.AlarmItem item = AlarmContent.AlarmItem.builder()
                    .type(AlarmTypeEnum.REJECTED)
                    .value(rejected)
                    .threshold(threshold)
                    .build();
            content.getAlarmItems().add(item);
        }
    }

    private void monitorCapacity(ExecutorStats stats, double threshold, AlarmContent content) {
        double capacity = (double) stats.getQueueSize() / (stats.getQueueSize() + stats.getRemainingQueueCapacity()) * 100;
        if (capacity >= threshold) {
            if (content.getAlarmItems() == null) {
                content.setAlarmItems(new ArrayList<>());
            }
            AlarmContent.AlarmItem item = AlarmContent.AlarmItem.builder()
                    .type(AlarmTypeEnum.CAPACITY)
                    .value(capacity)
                    .threshold(threshold)
                    .build();
            content.getAlarmItems().add(item);
        }
    }

    private void monitorLiveness(ExecutorStats stats, double threshold, AlarmContent content) {
        double liveness = (double) stats.getActiveCount() / stats.getMaximumPoolSize() * 100;
        if (liveness >= threshold) {
            if (content.getAlarmItems() == null) {
                content.setAlarmItems(new ArrayList<>());
            }
            AlarmContent.AlarmItem item = AlarmContent.AlarmItem.builder()
                    .type(AlarmTypeEnum.LIVENESS)
                    .value(liveness)
                    .threshold(threshold)
                    .build();
            content.getAlarmItems().add(item);
        }
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
            log.info("MONITOR_EXECUTOR stop");
        }
    }

    /**
     * Performs the actual collection for each executor.
     */
    protected abstract void doCollect(ExecutorStats stats, DynamicTpExecutor executor);
}