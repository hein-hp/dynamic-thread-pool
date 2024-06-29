package cn.hein.core.config;

import cn.hein.core.listener.DynamicTpInitListener;
import cn.hein.core.listener.DynamicTpRefreshListener;
import cn.hein.core.properties.DynamicTpProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import static org.springframework.util.Assert.notNull;

/**
 * Configuration for dynamic thread pool setup in the application.
 * Registers listeners to initialize and refresh thread pool settings based on configuration.
 *
 * @author hein
 */
@Slf4j
@Configuration
public class DynamicTpConfig {

    /**
     * Creates a DynamicTpInitListener bean if dynamic thread pools are enabled.
     *
     * @param properties The properties for configuring dynamic thread pools.
     * @return A DynamicTpInitListener bean or null if disabled.
     */
    @Bean
    public DynamicTpInitListener dynamicTpInitListener(@NonNull DynamicTpProperties properties) {
        notNull(properties, "DynamicTpProperties must not be null");

        if (properties.isEnabled()) {
            return new DynamicTpInitListener();
        } else {
            log.info("Dynamic thread pools are disabled.");
            return null;
        }
    }

    /**
     * Creates a DynamicTpRefreshListener bean if dynamic thread pools are enabled.
     *
     * @param properties The properties for configuring dynamic thread pools.
     * @param context    The ApplicationContext.
     * @return A DynamicTpRefreshListener bean or null if disabled.
     */
    @Bean
    public DynamicTpRefreshListener dynamicTpRefreshListener(@NonNull DynamicTpProperties properties,
                                                             @NonNull ApplicationContext context) {
        notNull(properties, "DynamicTpProperties must not be null");
        notNull(context, "ApplicationContext must not be null");

        if (properties.isEnabled()) {
            return new DynamicTpRefreshListener(context);
        } else {
            log.info("Dynamic thread pools are disabled.");
            return null;
        }
    }
}