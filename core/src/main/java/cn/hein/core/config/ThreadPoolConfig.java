package cn.hein.core.config;

import cn.hein.core.listener.DynamicTpInitListener;
import cn.hein.core.properties.DynamicTpProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for dynamic thread pools.
 *
 * @author hein
 */
@Slf4j
@Configuration
public class ThreadPoolConfig {

    /**
     * Initializes a listener for dynamic thread pool setup if enabled in properties.
     *
     * @param dynamicTpProperties the dynamic thread pool properties
     * @return DynamicTpInitListener if dynamic thread pools are enabled, otherwise null
     */
    @Bean
    public DynamicTpInitListener dynamicTpBeanListener(DynamicTpProperties dynamicTpProperties) {
        if (dynamicTpProperties.isEnable()) {
            return new DynamicTpInitListener(dynamicTpProperties);
        } else {
            log.info("Dynamic thread pool initialization is disabled according to the configuration.");
            return null;
        }
    }
}