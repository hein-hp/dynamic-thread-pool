package cn.hein.core.notifier;

import cn.hein.common.entity.alarm.AlarmContent;
import cn.hein.common.enums.alarm.NotifyTypeEnum;
import cn.hein.common.spring.ApplicationContextHolder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Context for managing notify strategies.
 *
 * @author hein
 */
@Component
public class StrategyContext implements InitializingBean {

    private static final Map<NotifyTypeEnum, Strategy> STRATEGY_MAP = new ConcurrentHashMap<>();

    /**
     * Dispatches a notification to the appropriate strategy.
     *
     * @param type      the notification type
     * @param content   the content of the notification
     * @param timestamp the timestamp of the notification
     */
    public void notify(NotifyTypeEnum type, AlarmContent content, long timestamp) {
        STRATEGY_MAP.get(type).notify(content, timestamp);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ApplicationContextHolder.getBeansOfType(Strategy.class)
                .forEach((beanName, strategy) -> STRATEGY_MAP.put(NotifyTypeEnum.from(strategy.name()), strategy));
    }
}
