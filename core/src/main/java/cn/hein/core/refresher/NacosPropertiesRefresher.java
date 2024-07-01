package cn.hein.core.refresher;

import cn.hein.common.entity.properties.DynamicTpProperties;
import cn.hein.common.toolkit.ThreadPoolInfoPrinter;
import cn.hein.core.DynamicTpContext;
import cn.hein.core.executor.DynamicTpExecutor;
import cn.hein.core.publisher.RefreshEventPublisher;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;

import static cn.hein.common.spring.ApplicationContextHolder.getBean;

/**
 * Nacos Properties Refresher
 *
 * @author hein
 */
@Slf4j
public class NacosPropertiesRefresher extends AbstractRefresher implements SmartApplicationListener {

    public NacosPropertiesRefresher(DynamicTpProperties properties,
                                    DynamicTpContext context) {
        super(properties, context);
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
        RefreshEventPublisher publisher = getBean(RefreshEventPublisher.class);
        publisher.publishEvent(getBean(DynamicTpProperties.class));
        ThreadPoolInfoPrinter.printThreadPoolInfo(getBean(DynamicTpExecutor.class));
    }

    @Override
    protected DynamicTpProperties beforeRefresh() {
        ThreadPoolInfoPrinter.printThreadPoolInfo(getBean(DynamicTpExecutor.class));
        DynamicTpProperties oldProp = getBean(DynamicTpProperties.class);
        return BeanUtil.toBean(oldProp, DynamicTpProperties.class);
    }
}
