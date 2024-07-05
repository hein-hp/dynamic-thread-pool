package cn.hein.core.listener;

import cn.hein.common.entity.properties.DynamicTpProperties;
import cn.hein.common.entity.properties.ExecutorProperties;
import cn.hein.common.enums.alarm.NotifyTypeEnum;
import cn.hein.common.equator.FieldInfo;
import cn.hein.core.context.NotifyPlatformContext;
import cn.hein.core.event.RefreshEvent;
import cn.hein.core.notifier.StrategyContext;
import cn.hein.core.support.PropertiesEqualHelper;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Refresh Event Listener
 *
 * @author hein
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshListener implements ApplicationListener<RefreshEvent> {

    private final StrategyContext strategies;
    private final NotifyPlatformContext pfContext;

    @Override
    public void onApplicationEvent(@NonNull RefreshEvent event) {
        try {
            // properties change notify
            if (event.getSource() instanceof DynamicTpProperties oldProp) {
                doNotify(DynamicTpProperties.getInstance(), oldProp, event.getTimestamp());
            }
        } catch (Exception e) {
            log.error("refresh event handle failed.", e);
        }
    }

    private void doNotify(DynamicTpProperties newProp, DynamicTpProperties oldProp, long timestamp) {
        if (PropertiesEqualHelper.equal(newProp, oldProp)) {
            return;
        }
        Map<String, ExecutorProperties> oldMap = oldProp.getExecutors()
                .stream()
                .collect(Collectors.toMap(ExecutorProperties::getThreadPoolName, v -> v));
        Map<String, List<FieldInfo>> diff = new HashMap<>();
        newProp.getExecutors().forEach(executorProp -> {
            List<FieldInfo> diffFields = PropertiesEqualHelper.getDifferentFields(executorProp, oldMap.get(executorProp.getThreadPoolName()));
            if (!diffFields.isEmpty()) {
                diff.put(executorProp.getThreadPoolName(), diffFields);
            }
        });
        // we don't need notify if the thread pool is not refreshed
        if (diff.isEmpty()) {
            return;
        }
        List<String> platformKeys = StrUtil.split(newProp.getChange().getPlatformKey(), ",");
        platformKeys.forEach(key -> {
            try {
                strategies.changeNotify(NotifyTypeEnum.from(pfContext.getPlatforms(key).getPlatform()), diff, timestamp, key);
            } catch (Exception e) {
                log.error("change notify failed.", e);
            }
        });
    }
}
