package cn.hein.core.context;

import cn.hein.common.entity.properties.DynamicTpProperties;
import cn.hein.common.entity.properties.ExecutorProperties;
import cn.hein.common.enums.executors.BlockingQueueTypeEnum;
import cn.hein.common.enums.executors.RejectionPolicyTypeEnum;
import cn.hein.common.queue.ResizeLinkedBlockingQueue;
import cn.hein.core.executor.DynamicTpExecutor;
import cn.hein.core.executor.NamedThreadFactory;
import cn.hein.core.executor.reject.RejectHandlerProxyFactory;
import cn.hein.core.monitor.MonitorController;
import cn.hein.core.spring.ApplicationContextHolder;
import cn.hein.core.support.PropertiesEqualHelper;
import cn.hutool.core.collection.CollUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static cn.hein.common.enums.executors.RejectionPolicyTypeEnum.getRejectionPolicyEnum;

/**
 * Context manager for dynamic thread pools.
 *
 * @author hein
 */
@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class DynamicTpContext {

    /**
     * Dynamic thread pool executor map.
     */
    private static final Map<String, DynamicTpExecutor> DYNAMIC_CONTEXT = new ConcurrentHashMap<>();

    private final DynamicTpProperties properties;
    private final MonitorController monitor;

    /**
     * Lists all registered dynamic thread pool names.
     *
     * @return A list of thread pool names.
     */
    public static List<String> listDynamicTp() {
        return CollUtil.newArrayList(DYNAMIC_CONTEXT.keySet());
    }

    /**
     * Retrieves a dynamic thread pool by its name.
     *
     * @param threadPoolName The name of the thread pool.
     * @return The DynamicTpExecutor instance.
     */
    public static DynamicTpExecutor getDynamicTp(final String threadPoolName) {
        return Optional.ofNullable(DYNAMIC_CONTEXT.get(threadPoolName))
                .orElseThrow(() -> new RuntimeException("Dynamic thread pool not found: " + threadPoolName));
    }

    /**
     * Registers a dynamic thread pool executor.
     *
     * @param executor The DynamicTpExecutor to be registered.
     */
    public static void registerDynamicTp(DynamicTpExecutor executor) {
        DYNAMIC_CONTEXT.putIfAbsent(executor.getThreadPoolName(), executor);
    }

    /**
     * Refreshes the dynamic thread pool configuration based on new properties.
     *
     * @param oldProp The previous properties.
     */
    public void refresh(DynamicTpProperties oldProp) {
        if (PropertiesEqualHelper.equal(oldProp, properties)) {
            return;
        }
        log.info("DynamicTp refresh initiated. Old properties: {}, New properties: {}", oldProp, properties);
        refreshExecutor();
        refreshMonitor(oldProp);
    }

    private void refreshMonitor(DynamicTpProperties oldProp) {
        if (properties.getMonitor().isEnabled() && !oldProp.getMonitor().isEnabled()) {
            monitor.start(properties);
        }
        if (!properties.getMonitor().isEnabled() && oldProp.getMonitor().isEnabled()) {
            monitor.stop();
        }
    }

    private void refreshExecutor() {
        // properties is the newest.
        properties.getExecutors().forEach(each -> {
            if (!DYNAMIC_CONTEXT.containsKey(each.getThreadPoolName()) ||
                    !ApplicationContextHolder.containsBean(each.getBeanName())) {
                throw new IllegalStateException("Inconsistent state detected between DynamicTpContext and ApplicationContext for thread pool: " + each.getThreadPoolName());
            }
            doRefreshExecutor(each);
        });
    }

    private void doRefreshExecutor(ExecutorProperties prop) {
        // Note: Current implementation does not support adding or removing executors dynamically.
        checkParam(prop);
        // Refresh existing executor
        if (!DYNAMIC_CONTEXT.containsKey(prop.getThreadPoolName())) {
            log.warn("no such DynamicTp named {}, maybe you change the thread-pool-name", prop.getThreadPoolName());
        }
        DynamicTpExecutor executor = DYNAMIC_CONTEXT.get(prop.getThreadPoolName());
        refreshTp(executor, prop);
        refreshBq(executor, prop);
        log.info("DynamicTp Executor refreshed: {}", prop.getThreadPoolName());
    }

    @SuppressWarnings("rawtypes")
    private void refreshBq(DynamicTpExecutor executor, ExecutorProperties prop) {
        if (!(executor.getQueue().getClass() == BlockingQueueTypeEnum.getClassByName(prop.getQueueType()))) {
            throw new IllegalStateException("can't modify queue type.");
        }
        if (executor.getQueue().size() != prop.getQueueCapacity()) {
            if (executor.getQueue().getClass() == BlockingQueueTypeEnum.getClassByName("ResizeLinkedBlockingQueue")) {
                ((ResizeLinkedBlockingQueue) executor.getQueue()).setCapacity(prop.getQueueCapacity());
            } else {
                throw new IllegalArgumentException("the queue can't modify capacity.");
            }
        }
    }

    private void refreshTp(DynamicTpExecutor executor, ExecutorProperties prop) {
        if (executor.getCorePoolSize() != prop.getCorePoolSize()) {
            executor.setCorePoolSize(prop.getCorePoolSize());
        }
        if (executor.getMaximumPoolSize() != prop.getMaximumPoolSize()) {
            executor.setMaximumPoolSize(prop.getMaximumPoolSize());
        }
        if (executor.getThreadFactory() instanceof NamedThreadFactory namedThreadFactory) {
            if (namedThreadFactory.getPrefix().equals(prop.getExecutorNamePrefix())) {
                executor.setThreadFactory(new NamedThreadFactory(prop.getExecutorNamePrefix(), 0L));
            }
        }
        if (executor.getKeepAliveTime(TimeUnit.NANOSECONDS) != prop.getTimeUnit().toNanos(prop.getKeepAliveTime())) {
            // super
            executor.setKeepAliveTime(prop.getKeepAliveTime(), prop.getTimeUnit());
            executor.setKeepAliveTime(prop.getKeepAliveTime());
            executor.setUnit(prop.getTimeUnit());
        }
        if (executor.allowsCoreThreadTimeOut() != prop.isAllowCoreThreadTimeOut()) {
            executor.allowCoreThreadTimeOut(prop.isAllowCoreThreadTimeOut());
        }
        // getProxy
        RejectionPolicyTypeEnum type = getRejectionPolicyEnum(prop.getRejectedExecutionHandler());
        executor.setOriginalHandlerType(type);
        executor.setRejectedExecutionHandler(RejectHandlerProxyFactory.getProxy(type));
        // execute timeout
        if (executor.getExecuteTimeOut() != prop.getExecuteTimeOut()) {
            executor.setExecuteTimeOut(prop.getExecuteTimeOut());
        }
    }

    private void checkParam(ExecutorProperties properties) {
        if (properties.getCorePoolSize() < 0 ||
                properties.getMaximumPoolSize() <= 0 ||
                properties.getMaximumPoolSize() < properties.getCorePoolSize() ||
                properties.getKeepAliveTime() < 0) {
            throw new IllegalArgumentException("[Refresh] Invalid thread pool configuration parameters.");
        }
    }
}