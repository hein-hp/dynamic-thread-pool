package cn.hein.core.listener;

import cn.hein.core.common.enums.executors.BlockingQueueTypeEnum;
import cn.hein.core.common.enums.executors.RejectionPolicyTypeEnum;
import cn.hein.core.dynamic.DynamicThreadPoolExecutor;
import cn.hein.core.properties.DynamicThreadPoolProperties;
import cn.hein.core.properties.ExecutorProperties;
import cn.hein.core.toolkit.TimeUnitConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import static cn.hein.core.toolkit.StringUtil.snakeCaseToCamelCase;

/**
 * 动态线程池注册 Bean 监听器
 *
 * @author hein
 */
@Slf4j
public class DynamicThreadPoolBeanListener implements ApplicationListener<ApplicationReadyEvent> {

    private final DynamicThreadPoolProperties dynamicThreadPoolProperties;

    public DynamicThreadPoolBeanListener(DynamicThreadPoolProperties dynamicThreadPoolProperties) {
        this.dynamicThreadPoolProperties = dynamicThreadPoolProperties;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ConfigurableListableBeanFactory beanFactory = event.getApplicationContext().getBeanFactory();
        dynamicThreadPoolProperties.getExecutors().forEach(each -> registerBean(beanFactory, each));
        log.info("Dynamic ThreadPool Executors registered successfully at {}", event.getTimestamp());
    }

    private void registerBean(ConfigurableListableBeanFactory beanFactory, ExecutorProperties properties) {
        String beanName = snakeCaseToCamelCase(properties.getThreadPoolName());
        if (beanFactory.containsBean(beanName)) {
            return;
        }
        beanFactory.registerSingleton(beanName, new DynamicThreadPoolExecutor(
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
