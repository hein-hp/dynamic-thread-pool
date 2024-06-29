package cn.hein.core.collector;

import cn.hein.common.entity.info.DynamicTpCollectInfo;
import cn.hein.core.executor.DynamicTpExecutor;
import cn.hein.core.publisher.DynamicTpCollectCompleteEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Concrete implementation of a dynamic thread pool collector.
 * This collector is responsible for collecting information about managed thread pools.
 *
 * @author hein
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicTpExecutorCollector extends AbstractDynamicTpCollector {

    private final DynamicTpCollectCompleteEventPublisher publisher;

    @Override
    protected DynamicTpCollectInfo doCollect(String beanName, DynamicTpExecutor executor) {
        return DynamicTpCollectInfo.builder()
                .beanName(beanName)
                .poolSize(executor.getPoolSize())
                .corePoolSize(executor.getCorePoolSize())
                .maximumPoolSize(executor.getMaximumPoolSize())
                .activeCount(executor.getActiveCount())
                .taskCount(executor.getTaskCount())
                .completedTaskCount(executor.getCompletedTaskCount())
                .largestPoolSize(executor.getLargestPoolSize())
                .queueType(executor.getQueue().getClass().getName())
                .queueCapacity(executor.getQueue().size())
                .remainingCapacity(executor.getQueue().remainingCapacity())
                .handlerType(executor.getRejectedExecutionHandler().getClass().getName())
                .build();
    }

    @Override
    protected void publish(DynamicTpCollectInfo collectInfo) {
        publisher.publishEvent(collectInfo);
    }
}