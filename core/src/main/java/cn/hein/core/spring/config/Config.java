package cn.hein.core.spring.config;

import cn.hein.common.entity.properties.DynamicTpProperties;
import cn.hein.core.DynamicTpContext;
import cn.hein.core.refresher.NacosPropertiesRefresher;
import cn.hein.core.refresher.Refresher;
import cn.hein.core.registry.DynamicTpBeanRegistry;
import cn.hein.core.spring.DynamicTpPostProcessor;
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
@ComponentScan(basePackages = {"cn.hein.core.publisher", "cn.hein.core.listener"})
public class Config {

    @Bean
    public DynamicTpContext dynamicTpContext(DynamicTpProperties properties) {
        return new DynamicTpContext(properties);
    }

    @Bean
    public DynamicTpProperties dynamicTpProperties() {
        return DynamicTpProperties.getInstance();
    }

    @Bean
    public DynamicTpBeanRegistry dynamicTpBeanRegistry(Environment environment) {
        return new DynamicTpBeanRegistry(environment);
    }

    @Bean
    public Refresher refresher(DynamicTpProperties properties, DynamicTpContext context) {
        return new NacosPropertiesRefresher(properties, context);
    }

    @Bean
    public DynamicTpPostProcessor dynamicTpPostProcessor() {
        return new DynamicTpPostProcessor();
    }
}
