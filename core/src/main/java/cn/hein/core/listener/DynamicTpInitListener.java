package cn.hein.core.listener;

import cn.hein.common.enums.executors.BlockingQueueTypeEnum;
import cn.hein.common.enums.executors.RejectionPolicyTypeEnum;
import cn.hein.common.toolkit.TimeUnitConvertUtil;
import cn.hein.core.collector.DynamicTpCollector;
import cn.hein.core.executor.DynamicTpExecutor;
import cn.hein.core.properties.DynamicTpProperties;
import cn.hein.core.properties.DynamicTpPropertiesHolder;
import cn.hein.core.properties.ExecutorProperties;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import static cn.hein.common.toolkit.StringUtil.kebabCaseToCamelCase;

/**
 * Listener responsible for registering dynamic thread pool beans upon application readiness.
 *
 * @author hein
 */
@Slf4j
public class DynamicTpInitListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ConfigurableApplicationContext context = event.getApplicationContext();
        DynamicTpProperties properties = context.getBean(DynamicTpProperties.class);
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

        beanFactory.registerSingleton("dynamicTpPropertiesHolder",
                new DynamicTpPropertiesHolder(BeanUtil.toBean(properties, DynamicTpProperties.class)));

        properties.getExecutors().forEach(each -> registerBean(beanFactory, each));
        log.info("Dynamic ThreadPool Executors registered successfully at {}", event.getTimestamp());

        if (properties.getMonitor().isEnabled()) {
            context.getBean(DynamicTpCollector.class).start(properties);
        }
    }

    private void registerBean(ConfigurableListableBeanFactory beanFactory, ExecutorProperties properties) {
        String beanName = kebabCaseToCamelCase(properties.getThreadPoolName());
        properties.setBeanName(beanName);
        if (beanFactory.containsBean(beanName)) {
            return;
        }
        beanFactory.registerSingleton(beanName, new DynamicTpExecutor(
                properties.getExecutorNamePrefix(),
                properties.getCorePoolSize(),
                properties.getMaximumPoolSize(),
                properties.getKeepAliveTime(),
                TimeUnitConvertUtil.convert(properties.getTimeUnit()),
                BlockingQueueTypeEnum.getBlockingQueue(properties.getQueueType(), properties.getQueueCapacity()),
                RejectionPolicyTypeEnum.getRejectionPolicy(properties.getRejectedExecutionHandler())
        ));
    }
}