package cn.hein.core.refresher;

import cn.hein.common.entity.properties.DynamicTpProperties;
import cn.hein.core.context.DynamicTpContext;
import cn.hein.core.context.NotifyPlatformContext;
import cn.hein.core.publisher.RefreshEventPublisher;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static cn.hein.core.spring.ApplicationContextHolder.getBean;

/**
 * Nacos Properties Refresher
 *
 * @author hein
 */
@Slf4j
@Component
public class NacosPropertiesRefresher extends AbstractRefresher implements SmartApplicationListener {

    public NacosPropertiesRefresher(DynamicTpProperties properties,
                                    DynamicTpContext tpContext,
                                    NotifyPlatformContext pfContext) {
        super(properties, tpContext, pfContext);
    }

    @Override
    public boolean supportsEventType(@NonNull Class<? extends ApplicationEvent> eventType) {
        return EnvironmentChangeEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationEvent event) {
        if (event instanceof EnvironmentChangeEvent eve && needRefresh(eve.getKeys())) {
            refresh(getBean(Environment.class));
        }
    }

    @Override
    protected void afterRefresh() {
        if (needNotify()) {
            RefreshEventPublisher publisher = getBean(RefreshEventPublisher.class);
            publisher.publishEvent(Optional.ofNullable(PROPERTIES_THREAD_LOCAL.get())
                    .orElseThrow(() -> new RuntimeException("no properties in PROPERTIES_THREAD_LOCAL.")));
        }
        PROPERTIES_THREAD_LOCAL.remove();
    }

    @Override
    protected void beforeRefresh() {
        PROPERTIES_THREAD_LOCAL.set(BeanUtil.toBean(getBean(DynamicTpProperties.class), DynamicTpProperties.class));
    }
}
