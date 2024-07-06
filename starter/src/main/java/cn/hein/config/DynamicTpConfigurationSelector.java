package cn.hein.config;

import cn.hein.core.spring.config.Config;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * Configuration Selector
 *
 * @author hein
 */
public class DynamicTpConfigurationSelector implements DeferredImportSelector, Ordered, EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }

    @Override
    @NonNull
    public String[] selectImports(@NonNull AnnotationMetadata metadata) {
        if (Boolean.getBoolean(environment.getProperty("dynamic.thread-pool.enabled"))) {
            return new String[]{};
        }
        return new String[]{
                Config.class.getName()
        };
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
