package cn.hein.core.monitor;

import cn.hein.common.entity.monitor.ExecutorStats;
import cn.hein.common.pattern.chain.Filter;
import cn.hein.common.pattern.chain.Handler;
import cn.hein.core.DynamicTpContext;
import cn.hein.core.executor.DynamicTpExecutor;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Liveness Monitor implementation for collecting statistics about the active threads and maximum pool size.
 *
 * @author hein
 */
@Slf4j
@Component
public class LivenessMonitor extends AbstractMonitor implements Filter<ExecutorStats> {

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String getName() {
        return "COLLECT";
    }

    @Override
    public void doFilter(ExecutorStats context, Handler<ExecutorStats> next) {
        String threadPoolName = context.getThreadPoolName();
        if (StrUtil.isBlank(threadPoolName)) {
            throw new IllegalArgumentException("Thread pool name cannot be blank.");
        }
        DynamicTpExecutor executor = DynamicTpContext.getDynamicTp(threadPoolName);
        doCollect(context, executor);
        log.info("Collected data for thread pool {}: Active Count - {}, Maximum Pool Size - {}",
                threadPoolName, context.getActiveCount(), context.getMaximumPoolSize());
        next.handle(context);
    }

    @Override
    protected void doCollect(ExecutorStats stats, DynamicTpExecutor executor) {
        stats.setActiveCount(executor.getActiveCount());
        stats.setMaximumPoolSize(executor.getMaximumPoolSize());
    }
}