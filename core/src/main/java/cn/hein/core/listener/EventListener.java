package cn.hein.core.listener;

import cn.hein.common.entity.alarm.AlarmContent;
import cn.hein.common.entity.properties.DynamicTpProperties;
import cn.hein.common.entity.properties.ExecutorProperties;
import cn.hein.common.enums.alarm.NotifyTypeEnum;
import cn.hein.core.context.NotifyPlatformContext;
import cn.hein.core.event.NotifyEvent;
import cn.hein.core.event.RefreshEvent;
import cn.hein.core.notifier.StrategyContext;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Event Listener
 *
 * @author hein
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EventListener implements GenericApplicationListener {

    private final StrategyContext strategies;
    private final NotifyPlatformContext pfContext;

    @Override
    public boolean supportsEventType(ResolvableType eventType) {
        Class<?> type = eventType.getRawClass();
        if (type != null) {
            return RefreshEvent.class.isAssignableFrom(type) || NotifyEvent.class.isAssignableFrom(type);
        }
        return false;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationEvent event) {
        try {
            if (event instanceof RefreshEvent refreshEvent) {
                // current do nothing
            } else if (event instanceof NotifyEvent notifyEvent) {
                doNotify(((AlarmContent) notifyEvent.getSource()), notifyEvent.getTimestamp());
            }
        } catch (Exception e) {
            log.error("event handle failed.", e);
        }
    }

    private void doNotify(AlarmContent content, long timestamp) {
        Map<String, ExecutorProperties> propMap = DynamicTpProperties.getInstance()
                .getExecutors().stream().collect(Collectors.toMap(ExecutorProperties::getThreadPoolName, v -> v));
        List<String> platformKeys = StrUtil.split(propMap.get(content.getThreadPoolName()).getNotify().getPlatformKey(), ",");
        platformKeys.forEach(key -> strategies.notify(NotifyTypeEnum.from(pfContext.getPlatforms(key).getPlatform()), content, timestamp));
    }
}
