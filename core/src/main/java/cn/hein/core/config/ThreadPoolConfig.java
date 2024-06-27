package cn.hein.core.config;

import cn.hein.core.listener.DynamicThreadPoolBeanListener;
import cn.hein.core.properties.DynamicThreadPoolProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 线程池配置类
 *
 * @author hein
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public DynamicThreadPoolBeanListener dynamicThreadPoolBeanListener(DynamicThreadPoolProperties dynamicThreadPoolProperties) {
        if (dynamicThreadPoolProperties.isEnable()) {
            return new DynamicThreadPoolBeanListener(dynamicThreadPoolProperties);
        } else {
            return null;
        }
    }
}
