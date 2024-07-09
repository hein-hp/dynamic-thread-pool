package cn.hein.core.executor.runnable;

import cn.hein.common.timer.HashedWheelTimer;
import cn.hein.common.timer.Timer;
import cn.hein.core.executor.DynamicTpExecutor;
import cn.hein.core.executor.NamedThreadFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The Manager for task execute timeout.
 *
 * @author hein
 */
@Slf4j
@Getter
@Component
public class TimeoutTaskManager {

    private final AtomicLong timeoutCycleCount = new AtomicLong(0L);

    private static final Timer timer = new HashedWheelTimer(new NamedThreadFactory("execute-timeout", 0L));

    public void startTimeOut(DynamicTpExecutor executor, Thread t, Runnable r) {
        timer.newTimeout(timeout -> {
            incCycleTimeoutCount(executor);
            log.warn("Thread: {} execute: {} timeout", t, r);
        }, executor.getExecuteTimeOut(), TimeUnit.MILLISECONDS);
    }

    public void incCycleTimeoutCount(DynamicTpExecutor executor) {
        // cycle incr
        timeoutCycleCount.incrementAndGet();
        // total incr
        incTimeoutCount(executor);
    }

    public void clearTimeoutCount() {
        timeoutCycleCount.set(0);
    }

    private void incTimeoutCount(DynamicTpExecutor executor) {
        executor.getTimeoutCount().incrementAndGet();
    }
}
