package cn.hein.core.config;

import cn.hein.core.properties.DynamicThreadPoolProperties;
import cn.hein.core.registry.DynamicThreadPoolBeanRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 线程池配置类
 *
 * @author hein
 */
@Configuration
@RequiredArgsConstructor
public class ThreadPoolConfig {

    private final DynamicThreadPoolProperties dynamicThreadPoolProperties;

    @Bean
    public DynamicThreadPoolBeanRegistry dynamicThreadPoolBeanRegistry() {
        if (dynamicThreadPoolProperties.isEnable()) {
            return new DynamicThreadPoolBeanRegistry(dynamicThreadPoolProperties);
        } else {
            return null;
        }
    }
}
