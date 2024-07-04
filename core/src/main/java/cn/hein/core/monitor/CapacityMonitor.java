package cn.hein.core.monitor;

import cn.hein.common.entity.monitor.ExecutorStats;
import cn.hein.common.pattern.chain.Filter;
import cn.hein.common.pattern.chain.Handler;
import cn.hein.core.context.DynamicTpContext;
import cn.hein.core.executor.DynamicTpExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Capacity Monitor implementation for collecting statistics about the queue size and remaining queue capacity
 *
 * @author hein
 */
@Slf4j
@Component
public class CapacityMonitor extends AbstractMonitor implements Filter<ExecutorStats> {

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getName() {
        return "COLLECT";
    }

    @Override
    public void doFilter(ExecutorStats context, Handler<ExecutorStats> next) {
        String threadPoolName = context.getThreadPoolName();
        DynamicTpExecutor executor = DynamicTpContext.getDynamicTp(threadPoolName);
        doCollect(context, executor);
        log.info("Collected data for thread pool {}: Queue Size - {}, Remaining Queue Capacity - {}",
                threadPoolName, context.getQueueSize(), context.getRemainingQueueCapacity());
        next.handle(context);
    }

    @Override
    protected void doCollect(ExecutorStats stats, DynamicTpExecutor executor) {
        stats.setQueueSize(executor.getQueue().size());
        stats.setRemainingQueueCapacity(executor.getQueue().remainingCapacity());
    }
}
