package cn.hein.core.registry;

import cn.hein.common.entity.properties.DynamicTpProperties;
import cn.hein.common.entity.properties.ExecutorProperties;
import cn.hein.common.enums.executors.BlockingQueueTypeEnum;
import cn.hein.common.enums.executors.RejectionPolicyTypeEnum;
import cn.hein.common.spring.BeanRegistryHelper;
import cn.hein.common.toolkit.StringUtil;
import cn.hein.core.executor.DynamicTpExecutor;
import cn.hein.core.executor.reject.RejectHandlerProxyFactory;
import cn.hein.core.support.PropertiesBindHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;

import static cn.hein.common.enums.executors.RejectionPolicyTypeEnum.getRejectionPolicyEnum;

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
        DynamicTpProperties properties = bindProperties();
        if (properties.isEnabled()) {
            properties.getExecutors().forEach(executor -> BeanRegistryHelper.registerIfAbsent(
                    registry,
                    executor.getBeanName(),
                    DynamicTpExecutor.class,
                    buildConstructorArgs(executor)
            ));
        }
    }

    private DynamicTpProperties bindProperties() {
        DynamicTpProperties properties = DynamicTpProperties.getInstance();
        PropertiesBindHelper.bindProperties(environment, properties);
        // set beanName
        properties.getExecutors()
                .forEach(executor -> executor.setBeanName(StringUtil.kebabCaseToCamelCase(executor.getThreadPoolName())));
        return properties;
    }

    private Object[] buildConstructorArgs(ExecutorProperties prop) {
        checkParam(prop);
        RejectionPolicyTypeEnum originRejectionPolicy = getRejectionPolicyEnum(prop.getRejectedExecutionHandler());
        return new Object[]{
                prop.getThreadPoolName(),
                prop.getExecutorNamePrefix(),
                prop.getCorePoolSize(),
                prop.getMaximumPoolSize(),
                prop.getKeepAliveTime(),
                prop.getTimeUnit(),
                BlockingQueueTypeEnum.getBlockingQueue(prop.getQueueType(), prop.getQueueCapacity()),
                // getProxy
                RejectHandlerProxyFactory.getProxy(originRejectionPolicy),
                originRejectionPolicy,
                prop.getExecuteTimeOut()
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