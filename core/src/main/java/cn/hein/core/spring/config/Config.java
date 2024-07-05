package cn.hein.core.spring.config;

import cn.hein.common.entity.properties.DynamicTpProperties;
import cn.hein.core.registry.DynamicTpBeanRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Spring Config
 *
 * @author hein
 */
@Configuration
@ComponentScan(basePackages = {"cn.hein.core"})
public class Config {

    @Bean
    public DynamicTpProperties dynamicTpProperties() {
        return DynamicTpProperties.getInstance();
    }

    @Bean
    public DynamicTpBeanRegistry dynamicTpBeanRegistry(Environment environment) {
        return new DynamicTpBeanRegistry(environment);
    }
}
