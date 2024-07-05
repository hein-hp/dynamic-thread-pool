package cn.hein.core.notifier;

import cn.hein.common.entity.alarm.AlarmContent;
import cn.hein.common.enums.alarm.NotifyTypeEnum;
import cn.hein.common.equator.FieldInfo;
import cn.hein.common.spring.ApplicationContextHolder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;
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
     * @param sendKey   the send key of the notification
     * @throws Exception the exception
     */
    public void alarmNotify(NotifyTypeEnum type, AlarmContent content, long timestamp, String sendKey) throws Exception {
        STRATEGY_MAP.get(type).alarmNotify(content, timestamp, sendKey);
    }

    /**
     * Dispatches a notification to the appropriate strategy.
     *
     * @param type          the notification type
     * @param changedFields the changed fields
     * @param timestamp     the timestamp of the notification
     * @param sendKey       the send key of the notification
     * @throws Exception the exception
     */
    public void changeNotify(NotifyTypeEnum type, Map<String, List<FieldInfo>> changedFields, long timestamp, String sendKey) throws Exception {
        STRATEGY_MAP.get(type).changeNotify(changedFields, timestamp, sendKey);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ApplicationContextHolder.getBeansOfType(Strategy.class)
                .forEach((beanName, strategy) -> STRATEGY_MAP.put(NotifyTypeEnum.from(strategy.name()), strategy));
    }
}
