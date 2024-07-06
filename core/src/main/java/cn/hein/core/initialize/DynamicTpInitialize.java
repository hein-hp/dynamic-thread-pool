package cn.hein.core.initialize;

import cn.hein.common.entity.properties.DynamicTpProperties;
import cn.hein.common.pattern.chain.Filter;
import cn.hein.core.context.NotifyPlatformContext;
import cn.hein.core.executor.DynamicTpExecutor;
import cn.hein.core.monitor.AbstractMonitor;
import cn.hein.core.monitor.MonitorController;
import cn.hein.core.notifier.NotifyManager;
import cn.hein.core.spring.ApplicationContextHolder;
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
    private final MonitorController monitor;

    @Override
    public void afterPropertiesSet() throws Exception {
        initDependsOnBeans(new Class[]{DynamicTpExecutor.class, Filter.class});
        initMonitorExecutor(prop);
        initPlatform();
        initNotifyManager();
    }

    private void initNotifyManager() {
        NotifyManager.refresh();
    }

    private void initPlatform() {
        NotifyPlatformContext ctx = ApplicationContextHolder.getBean(NotifyPlatformContext.class);
        ctx.refresh();
    }

    /**
     * Initialize the beans that the {@link AbstractMonitor#MONITOR_EXECUTOR} depends on.
     *
     * @param classes the classes of the beans that the MonitorExecutor depends on.
     */
    private void initDependsOnBeans(Class<?>[] classes) {
        for (Class<?> clazz : classes) {
            ApplicationContextHolder.getBeansOfType(clazz);
        }
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