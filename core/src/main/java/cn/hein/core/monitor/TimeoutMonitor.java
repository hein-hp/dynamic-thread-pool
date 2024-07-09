package cn.hein.core.monitor;

import cn.hein.common.entity.monitor.ExecutorStats;
import cn.hein.common.pattern.chain.Filter;
import cn.hein.common.pattern.chain.Handler;
import cn.hein.core.context.DynamicTpContext;
import cn.hein.core.executor.DynamicTpExecutor;
import cn.hein.core.executor.runnable.TimeoutTaskManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Timeout Monitor implementation for collecting statistics about the task execute timeout.
 *
 * @author hein
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TimeoutMonitor extends AbstractMonitor implements Filter<ExecutorStats> {

    private final TimeoutTaskManager manager;

    @Override
    public int getOrder() {
        return 4;
    }

    @Override
    public String getName() {
        return "COLLECT";
    }

    @Override
    public void doFilter(ExecutorStats context, Handler<ExecutorStats> next) {
        String threadPoolName = context.getThreadPoolName();
        DynamicTpExecutor executor = DynamicTpContext.getDynamicTp(threadPoolName);
        log.info("Collected data for thread pool {}: timeoutCycleCount - {}, timeoutCount - {}",
                threadPoolName, manager.getTimeoutCycleCount().get(), executor.getTimeoutCount().get());
        doCollect(context, executor);
        next.handle(context);
    }

    @Override
    protected void doCollect(ExecutorStats stats, DynamicTpExecutor executor) {
        stats.setTimeoutCycleCount(manager.getTimeoutCycleCount().get());
        stats.setTimeoutCount(executor.getTimeoutCount().get());
        manager.clearTimeoutCount();
    }
}
