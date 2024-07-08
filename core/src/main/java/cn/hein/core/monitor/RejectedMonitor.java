package cn.hein.core.monitor;

import cn.hein.common.entity.monitor.ExecutorStats;
import cn.hein.common.pattern.chain.Filter;
import cn.hein.common.pattern.chain.Handler;
import cn.hein.core.context.DynamicTpContext;
import cn.hein.core.executor.DynamicTpExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Rejected Monitor implementation for collecting statistics about the rejected count.
 *
 * @author hein
 */
@Slf4j
@Component
public class RejectedMonitor extends AbstractMonitor implements Filter<ExecutorStats> {

    private final AtomicLong rejectedCycleCount = new AtomicLong(0L);

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getName() {
        return "COLLECT";
    }

    @Override
    public void doFilter(ExecutorStats context, Handler<ExecutorStats> next) {
        String threadPoolName = context.getThreadPoolName();
        DynamicTpExecutor executor = DynamicTpContext.getDynamicTp(threadPoolName);
        log.info("Collected data for thread pool {}: rejectedCycleCount - {}, rejectedCount - {}",
                threadPoolName, rejectedCycleCount.get(), executor.getRejectedCount());
        doCollect(context, executor);
        next.handle(context);
    }

    @Override
    protected void doCollect(ExecutorStats stats, DynamicTpExecutor executor) {
        stats.setRejectedCycleCount(rejectedCycleCount.get());
        stats.setRejectedCount(executor.getRejectedCount().get());
        clearRejectCount();
    }

    public void incCycleRejectedCount(DynamicTpExecutor executor) {
        // cycle incr
        rejectedCycleCount.incrementAndGet();
        // total incr
        incRejectedCount(executor);
    }

    public void clearRejectCount() {
        rejectedCycleCount.set(0);
    }

    private void incRejectedCount(DynamicTpExecutor executor) {
        executor.getRejectedCount().incrementAndGet();
    }
}
