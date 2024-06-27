package cn.hein.core.registry;

import cn.hein.common.enums.executors.BlockingQueueTypeEnum;
import cn.hein.common.enums.executors.RejectionPolicyTypeEnum;
import cn.hein.core.executor.DynamicTpExecutor;
import cn.hein.core.properties.DynamicTpProperties;
import cn.hein.common.toolkit.TimeUnitConvertUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.PriorityOrdered;

/**
 * Dynamic ThreadPool Bean registrar (not functional, deprecated).
 *
 * @author hein
 */
@Deprecated
public class DynamicTpBeanRegistry implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

    /**
     * Dynamic ThreadPool configuration properties.
     */
    private final DynamicTpProperties dynamicTpProperties;

    public DynamicTpBeanRegistry(DynamicTpProperties dynamicTpProperties) {
        this.dynamicTpProperties = dynamicTpProperties;
    }

    private void registerThreadPoolBeans(GenericApplicationContext context) {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        dynamicTpProperties.getExecutors()
                .stream()
                .filter(each -> beanFactory.containsBeanDefinition(each.getThreadPoolName()))
                .forEach(each -> {
                    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(DynamicTpExecutor.class)
                            .addConstructorArgValue(each.getExecutorNamePrefix())
                            .addConstructorArgValue(each.getCorePoolSize())
                            .addConstructorArgValue(each.getMaximumPoolSize())
                            .addConstructorArgValue(each.getKeepAliveTime())
                            .addConstructorArgValue(TimeUnitConvertUtil.convert(each.getTimeUnit()))
                            .addConstructorArgValue(BlockingQueueTypeEnum.getBlockingQueue(each.getQueueType(), each.getQueueCapacity()))
                            .addConstructorArgValue(RejectionPolicyTypeEnum.getRejectionPolicy(each.getRejectedExecutionHandler()));
                    beanFactory.registerBeanDefinition(each.getThreadPoolName(), builder.getBeanDefinition());
                });
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (registry instanceof GenericApplicationContext) {
            registerThreadPoolBeans((GenericApplicationContext) registry);
        } else {
            throw new FatalBeanException("Unsupported BeanDefinitionRegistry type: " + registry.getClass().getName());
        }
    }

    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // No operation needed here
    }
}