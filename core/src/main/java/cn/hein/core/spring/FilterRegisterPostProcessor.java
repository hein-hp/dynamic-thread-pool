package cn.hein.core.spring;

import cn.hein.common.pattern.chain.Filter;
import cn.hein.common.pattern.chain.FilterContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;

/**
 * Post-processor for Spring Beans to intercept instances of Filter and
 * register them in the FilterContext.
 *
 * @author hein
 */
public class FilterRegisterPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof Filter<?> filter) {
            FilterContext.registerFilter(filter.getName(), filter);
        }
        return bean;
    }
}
