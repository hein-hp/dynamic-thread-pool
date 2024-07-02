package cn.hein.core.initialize;

import cn.hein.common.entity.properties.DynamicTpProperties;
import cn.hein.core.monitor.LivenessMonitor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Initializes dynamic thread pool settings and starts the monitor if enabled.
 *
 * @author hein
 */
@Component
@RequiredArgsConstructor
public class DynamicTpInitialize implements InitializingBean {

    private final DynamicTpProperties prop;
    private final LivenessMonitor monitor;

    @Override
    public void afterPropertiesSet() throws Exception {
        initMonitorExecutor(prop);
    }

    /**
     * Checks if monitoring is enabled in the properties and starts the monitor if true.
     *
     * @param prop the properties object containing the monitor configuration.
     */
    private void initMonitorExecutor(DynamicTpProperties prop) {
        if (prop.getMonitor().isEnabled()) {
            monitor.start(prop);
        }
    }
}