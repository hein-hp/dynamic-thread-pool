package cn.hein.core.listener;

import cn.hein.common.entity.alarm.AlarmContent;
import cn.hein.common.entity.properties.DynamicTpProperties;
import cn.hein.common.entity.properties.ExecutorProperties;
import cn.hein.common.enums.alarm.NotifyTypeEnum;
import cn.hein.core.context.NotifyPlatformContext;
import cn.hein.core.event.NotifyEvent;
import cn.hein.core.notifier.StrategyContext;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Notify Event Listener
 *
 * @author hein
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotifyListener implements ApplicationListener<NotifyEvent> {

    private final StrategyContext strategies;
    private final NotifyPlatformContext pfContext;

    @Override
    public void onApplicationEvent(@NonNull NotifyEvent event) {
        try {
            if (event.getSource() instanceof AlarmContent content) {
                doNotify(content, event.getTimestamp());
            }
        } catch (Exception e) {
            log.error("notify event handle failed.", e);
        }
    }

    private void doNotify(AlarmContent content, long timestamp) {
        Map<String, ExecutorProperties> propMap = DynamicTpProperties.getInstance()
                .getExecutors().stream().collect(Collectors.toMap(ExecutorProperties::getThreadPoolName, v -> v));
        List<String> platformKeys = StrUtil.split(propMap.get(content.getThreadPoolName()).getNotify().getPlatformKey(), ",");
        platformKeys.forEach(key -> {
            try {
                strategies.alarmNotify(NotifyTypeEnum.from(pfContext.getPlatforms(key).getPlatform()), content, timestamp, key);
            } catch (Exception e) {
                throw new RuntimeException("alarm notify failed.", e);
            }
        });
    }
}
