package cn.hein.core.listener;

import cn.hein.core.common.enums.executors.BlockingQueueTypeEnum;
import cn.hein.core.common.enums.executors.RejectionPolicyTypeEnum;
import cn.hein.core.dynamic.DynamicTpExecutor;
import cn.hein.core.properties.DynamicTpProperties;
import cn.hein.core.properties.ExecutorProperties;
import cn.hein.core.toolkit.TimeUnitConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import static cn.hein.core.toolkit.StringUtil.snakeCaseToCamelCase;

/**
 * Listener responsible for registering dynamic thread pool beans upon application readiness.
 *
 * @author hein
 */
@Slf4j
public class DynamicTpInitListener implements ApplicationListener<ApplicationReadyEvent> {

    private final DynamicTpProperties dynamicTpProperties;

    public DynamicTpInitListener(DynamicTpProperties dynamicTpProperties) {
        this.dynamicTpProperties = dynamicTpProperties;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ConfigurableListableBeanFactory beanFactory = event.getApplicationContext().getBeanFactory();
        dynamicTpProperties.getExecutors().forEach(each -> registerBean(beanFactory, each));
        log.info("Dynamic ThreadPool Executors registered successfully at {}", event.getTimestamp());
    }

    private void registerBean(ConfigurableListableBeanFactory beanFactory, ExecutorProperties properties) {
        String beanName = snakeCaseToCamelCase(properties.getThreadPoolName());
        properties.setBeanName(beanName);
        if (beanFactory.containsBean(beanName)) {
            return;
        }
        beanFactory.registerSingleton(beanName, new DynamicTpExecutor(
                properties.getThreadPoolName(),
                properties.getCorePoolSize(),
                properties.getMaximumPoolSize(),
                properties.getKeepAliveTime(),
                TimeUnitConvertUtil.convert(properties.getTimeUnit()),
                BlockingQueueTypeEnum.getBlockingQueue(properties.getQueueType(), properties.getQueueCapacity()),
                RejectionPolicyTypeEnum.getRejectionPolicy(properties.getRejectedExecutionHandler())
        ));
    }
}