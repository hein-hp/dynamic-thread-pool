package cn.hein.core.registry;

import cn.hein.core.common.enums.dynamic.BlockingQueueTypeEnum;
import cn.hein.core.common.enums.dynamic.RejectionPolicyTypeEnum;
import cn.hein.core.dynamic.DynamicThreadPoolExecutor;
import cn.hein.core.properties.DynamicThreadPoolProperties;
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
 * 动态线程池 Bean 注册器（不可用，废弃）
 *
 * @author hein
 */
@Deprecated
public class DynamicThreadPoolBeanRegistry implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

    /**
     * 动态线程池配置
     */
    private final DynamicThreadPoolProperties dynamicThreadPoolProperties;

    public DynamicThreadPoolBeanRegistry(DynamicThreadPoolProperties dynamicThreadPoolProperties) {
        this.dynamicThreadPoolProperties = dynamicThreadPoolProperties;
    }

    private void registerThreadPoolBeans(GenericApplicationContext context) {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        dynamicThreadPoolProperties.getExecutors()
                .stream()
                .filter(each -> beanFactory.containsBeanDefinition(each.getThreadPoolName())).forEach(each -> {
                    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(DynamicThreadPoolExecutor.class)
                            .addConstructorArgValue(each.getExecutorNamePrefix())
                            .addConstructorArgValue(each.getCorePoolSize())
                            .addConstructorArgValue(each.getMaximumPoolSize())
                            .addConstructorArgValue(each.getKeepAliveTime())
                            .addConstructorArgValue(each.getTimeUnit())
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
        // No-op
    }
}
