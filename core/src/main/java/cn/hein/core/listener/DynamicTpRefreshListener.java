package cn.hein.core.listener;

import cn.hein.common.enums.executors.RejectionPolicyTypeEnum;
import cn.hein.common.queue.ResizeLinkedBlockingQueue;
import cn.hein.common.toolkit.TimeUnitConvertUtil;
import cn.hein.core.collector.DynamicTpCollector;
import cn.hein.core.event.NacosConfigRefreshEvent;
import cn.hein.core.executor.DynamicTpExecutor;
import cn.hein.core.properties.DynamicTpProperties;
import cn.hein.core.properties.DynamicTpPropertiesHolder;
import cn.hein.core.properties.ExecutorProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Dynamic ThreadPool Configuration Listener responsible for updating thread pool parameters dynamically.
 *
 * @author hein
 */
@Slf4j
public class DynamicTpRefreshListener implements ApplicationListener<NacosConfigRefreshEvent> {

    private final ApplicationContext context;

    public DynamicTpRefreshListener(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void onApplicationEvent(NacosConfigRefreshEvent event) {
        DynamicTpPropertiesHolder holder = context.getBean(DynamicTpPropertiesHolder.class);
        DynamicTpCollector collector = context.getBean(DynamicTpCollector.class);
        DynamicTpProperties updateProps = event.getMessage();
        Map<String, ExecutorProperties> executorMap = holder.getDynamicTpProperties().getExecutors().stream()
                .collect(Collectors.toMap(ExecutorProperties::getBeanName, v -> v));
        for (ExecutorProperties prop : updateProps.getExecutors()) {
            try {
                refreshExecutorProperties(prop, executorMap.get(prop.getBeanName()));
            } catch (IllegalAccessException e) {
                log.error("Failed to refresh dynamic thread pool parameters.", e);
            }
        }
        collector.start(updateProps);
    }

    @SuppressWarnings("rawtypes")
    private void refreshExecutorProperties(ExecutorProperties updatedProps, ExecutorProperties currentProps) throws IllegalAccessException {
        if (!context.containsBean(updatedProps.getBeanName())) {
            throw new IllegalAccessException("Bean not found.");
        }
        if (!updatedProps.getThreadPoolName().equals(currentProps.getThreadPoolName())) {
            throw new IllegalAccessException("Cannot modify the thread pool name.");
        }
        if (!updatedProps.getQueueType().equals(currentProps.getQueueType())) {
            throw new IllegalAccessException("Unsupported queue type modification.");
        }
        if (context.getBean(updatedProps.getBeanName()) instanceof DynamicTpExecutor executor) {
            if (updatedProps.getCorePoolSize() != currentProps.getCorePoolSize()) {
                executor.setCorePoolSize(updatedProps.getCorePoolSize());
                currentProps.setCorePoolSize(updatedProps.getCorePoolSize());
            }
            if (updatedProps.getMaximumPoolSize() != currentProps.getMaximumPoolSize()) {
                executor.setMaximumPoolSize(updatedProps.getMaximumPoolSize());
                currentProps.setMaximumPoolSize(updatedProps.getMaximumPoolSize());
            }
            if (updatedProps.getKeepAliveTime() != currentProps.getKeepAliveTime() || !updatedProps.getTimeUnit().equals(currentProps.getTimeUnit())) {
                executor.setKeepAliveTime(updatedProps.getKeepAliveTime(), TimeUnitConvertUtil.convert(updatedProps.getTimeUnit()));
                currentProps.setKeepAliveTime(updatedProps.getKeepAliveTime());
                currentProps.setTimeUnit(updatedProps.getTimeUnit());
            }
            if (updatedProps.getQueueCapacity() != currentProps.getQueueCapacity()) {
                if (executor.getQueue() instanceof ResizeLinkedBlockingQueue queue) {
                    queue.setCapacity(updatedProps.getQueueCapacity());
                    currentProps.setQueueCapacity(updatedProps.getQueueCapacity());
                } else {
                    throw new IllegalAccessException("Unsupported queue capacity modification.");
                }
            }
            if (!updatedProps.getRejectedExecutionHandler().equals(currentProps.getRejectedExecutionHandler())) {
                executor.setRejectedExecutionHandler(RejectionPolicyTypeEnum.getRejectionPolicy(updatedProps.getRejectedExecutionHandler()));
                currentProps.setRejectedExecutionHandler(updatedProps.getRejectedExecutionHandler());
            }
            if (!updatedProps.getExecutorNamePrefix().equals(currentProps.getExecutorNamePrefix())) {
                executor.setThreadFactory(new DynamicTpExecutor.NamedThreadFactory(updatedProps.getExecutorNamePrefix(), 0L));
                currentProps.setExecutorNamePrefix(updatedProps.getExecutorNamePrefix());
            }
        }
    }
}
