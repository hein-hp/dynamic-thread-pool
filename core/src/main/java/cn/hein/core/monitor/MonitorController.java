package cn.hein.core.monitor;

import cn.hein.common.entity.monitor.ExecutorStats;
import cn.hein.core.executor.DynamicTpExecutor;
import org.springframework.stereotype.Component;

/**
 * Make Control For Monitor
 *
 * @author hein
 */
@Component
public class MonitorController extends AbstractMonitor {

    @Override
    protected void doCollect(ExecutorStats stats, DynamicTpExecutor executor) {
        // do nothing
    }
}
