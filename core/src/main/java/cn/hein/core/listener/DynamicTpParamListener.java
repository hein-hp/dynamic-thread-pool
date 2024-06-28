package cn.hein.core.listener;

import cn.hein.common.enums.executors.RejectionPolicyTypeEnum;
import cn.hein.common.queue.ResizeLinkedBlockingQueue;
import cn.hein.common.toolkit.TimeUnitConvertUtil;
import cn.hein.core.event.DynamicTpRefreshEvent;
import cn.hein.core.executor.DynamicTpExecutor;
import cn.hein.core.properties.DynamicTpProperties;
import cn.hein.core.properties.DynamicTpPropertiesHolder;
import cn.hein.core.properties.ExecutorProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

import static cn.hein.common.spring.ApplicationContextHolder.containsBean;
import static cn.hein.common.spring.ApplicationContextHolder.getBean;

/**
 * Dynamic ThreadPool Configuration Listener responsible for updating thread pool parameters dynamically.
 *
 * @author hein
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicTpParamListener implements ApplicationListener<DynamicTpRefreshEvent> {

    @Override
    public void onApplicationEvent(DynamicTpRefreshEvent event) {
        DynamicTpPropertiesHolder dynamicTpPropertiesHolder = getBean(DynamicTpPropertiesHolder.class);
        DynamicTpProperties properties = event.getMessage();
        Map<String, ExecutorProperties> executorMap = dynamicTpPropertiesHolder.getDynamicTpProperties().getExecutors().stream()
                .collect(Collectors.toMap(ExecutorProperties::getBeanName, v -> v));
        for (ExecutorProperties prop : properties.getExecutors()) {
            try {
                refreshExecutorProperties(prop, executorMap.get(prop.getBeanName()));
            } catch (IllegalAccessException e) {
                log.error("Failed to refresh dynamic thread pool parameters.", e);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private void refreshExecutorProperties(ExecutorProperties updatedProps, ExecutorProperties currentProps) throws IllegalAccessException {
        if (!containsBean(updatedProps.getBeanName())) {
            throw new IllegalAccessException("Bean not found.");
        }
        if (!updatedProps.getThreadPoolName().equals(currentProps.getThreadPoolName())) {
            throw new IllegalAccessException("Cannot modify the thread pool name.");
        }
        if (!updatedProps.getQueueType().equals(currentProps.getQueueType())) {
            throw new IllegalAccessException("Unsupported queue type modification.");
        }
        if (getBean(updatedProps.getBeanName()) instanceof DynamicTpExecutor executor) {
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
