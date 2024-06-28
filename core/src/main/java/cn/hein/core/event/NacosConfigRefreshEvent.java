package cn.hein.core.event;

import cn.hein.core.properties.DynamicTpProperties;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Event triggered when nacos configurations are refreshed.
 *
 * @author hein
 */
@Getter
public class NacosConfigRefreshEvent extends ApplicationEvent {

    private final DynamicTpProperties message;

    /**
     * Constructs a new NacosConfigRefreshEvent.
     *
     * @param source     the object on which the event initially occurred or with which the event is associated (never {@code null})
     * @param properties the dynamic thread pool properties that triggered the refresh (never {@code null})
     */
    public NacosConfigRefreshEvent(Object source, DynamicTpProperties properties) {
        super(source);
        this.message = properties;
    }
}