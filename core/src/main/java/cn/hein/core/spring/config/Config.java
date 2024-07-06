package cn.hein.core.spring.config;

import cn.hein.common.entity.properties.DynamicTpProperties;
import cn.hein.core.registry.DynamicTpBeanRegistry;
import cn.hein.core.spring.ApplicationContextHolder;
import cn.hein.core.spring.DynamicTpPostProcessor;
import cn.hein.core.spring.FilterRegisterPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
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

    @Bean
    @ConditionalOnMissingBean(ApplicationContextHolder.class)
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }

    @Bean
    @DependsOn("applicationContextHolder")
    public DynamicTpPostProcessor dynamicTpPostProcessor() {
        return new DynamicTpPostProcessor();
    }

    @Bean
    public FilterRegisterPostProcessor filterRegisterPostProcessor() {
        return new FilterRegisterPostProcessor();
    }
}
