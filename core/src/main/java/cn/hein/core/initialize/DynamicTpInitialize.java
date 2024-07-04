package cn.hein.core.initialize;

import cn.hein.common.entity.properties.DynamicTpProperties;
import cn.hein.common.pattern.chain.Filter;
import cn.hein.common.spring.ApplicationContextHolder;
import cn.hein.core.context.NotifyPlatformContext;
import cn.hein.core.executor.DynamicTpExecutor;
import cn.hein.core.monitor.AbstractMonitor;
import cn.hein.core.monitor.MonitorController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
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
    private final ApplicationContext context;

    @Override
    public void afterPropertiesSet() throws Exception {
        // init ApplicationContextHolder
        context.getBean(ApplicationContextHolder.class);
        initDependsOnBeans(new Class[]{DynamicTpExecutor.class, Filter.class});
        initMonitorExecutor(prop);
        initPlatform();
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