package cn.hein.core.registry;

import cn.hein.common.entity.properties.DynamicTpProperties;
import cn.hein.common.entity.properties.ExecutorProperties;
import cn.hein.common.enums.executors.BlockingQueueTypeEnum;
import cn.hein.common.enums.executors.RejectionPolicyTypeEnum;
import cn.hein.common.spring.BeanRegistryHelper;
import cn.hein.common.toolkit.StringUtil;
import cn.hein.core.executor.DynamicTpExecutor;
import cn.hein.core.support.PropertiesBindHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;

/**
 * Registers dynamic thread pool beans based on configuration properties.
 *
 * @author hein
 */
@Slf4j
public class DynamicTpBeanRegistry implements BeanDefinitionRegistryPostProcessor {

    private final Environment environment;

    public DynamicTpBeanRegistry(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        DynamicTpProperties properties = DynamicTpProperties.getInstance();
        PropertiesBindHelper.bindProperties(environment, properties);
        // set beanName
        properties.getExecutors()
                .forEach(executor -> executor.setBeanName(StringUtil.kebabCaseToCamelCase(executor.getThreadPoolName())));
        if (properties.isEnabled()) {
            properties.getExecutors().forEach(executor -> BeanRegistryHelper.registerIfAbsent(
                    registry,
                    executor.getBeanName(),
                    DynamicTpExecutor.class,
                    buildConstructorArgs(executor)
            ));
        }
    }

    /**
     * Builds the constructor arguments for the DynamicTpExecutor bean.
     *
     * @param prop ExecutorProperties for configuring the thread pool
     * @return an array of Object representing the constructor arguments
     */
    private Object[] buildConstructorArgs(ExecutorProperties prop) {
        checkParam(prop);
        return new Object[]{
                prop.getThreadPoolName(),
                prop.getExecutorNamePrefix(),
                prop.getCorePoolSize(),
                prop.getMaximumPoolSize(),
                prop.getKeepAliveTime(),
                prop.getTimeUnit(),
                BlockingQueueTypeEnum.getBlockingQueue(prop.getQueueType(), prop.getQueueCapacity()),
                RejectionPolicyTypeEnum.getRejectionPolicy(prop.getRejectedExecutionHandler())
        };
    }

    private void checkParam(ExecutorProperties prop) {
        if (prop.getCorePoolSize() < 0 ||
                prop.getMaximumPoolSize() <= 0 ||
                prop.getMaximumPoolSize() < prop.getCorePoolSize() ||
                prop.getKeepAliveTime() < 0) {
            throw new IllegalArgumentException("[Init] Invalid thread pool configuration parameters.");
        }
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // No implementation needed here
    }
}