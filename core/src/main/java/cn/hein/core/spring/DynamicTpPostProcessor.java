package cn.hein.core.spring;

import cn.hein.common.entity.properties.DynamicTpProperties;
import cn.hein.common.entity.properties.ExecutorProperties;
import cn.hein.core.context.DynamicTpContext;
import cn.hein.core.executor.DynamicTpExecutor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;

import java.util.Map;
import java.util.stream.Collectors;

import static cn.hein.core.spring.ApplicationContextHolder.getBean;

/**
 * Post-processor for Spring Beans to intercept instances of DynamicTpExecutor and
 * register them in the DynamicTpContext.
 *
 * @author hein
 */
public class DynamicTpPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof DynamicTpExecutor executor) {
            // Register the DynamicTpExecutor in the DynamicTpContext
            DynamicTpContext.registerDynamicTp(executor);
            // handle allowCoreThreadTimeout property, maybe need to be optimized in the future
            Map<String, ExecutorProperties> map = getBean(DynamicTpProperties.class).getExecutors()
                    .stream().collect(Collectors.toMap(ExecutorProperties::getBeanName, v -> v));
            if (map.containsKey(beanName) && map.get(beanName).isAllowCoreThreadTimeOut()) {
                executor.allowCoreThreadTimeOut(true);
            }
            return executor;
        }
        return bean;
    }
}