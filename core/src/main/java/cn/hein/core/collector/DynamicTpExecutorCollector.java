package cn.hein.core.collector;

import cn.hein.core.executor.DynamicTpExecutor;
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
public class DynamicTpExecutorCollector extends AbstractDynamicTpCollector {

    /**
     * Collects information about a specific thread pool.
     *
     * @param beanName The name of the bean representing the thread pool.
     * @param executor The thread pool executor to collect information from.
     */
    @Override
    protected void doCollect(String beanName, DynamicTpExecutor executor) {
        // TODO
    }
}