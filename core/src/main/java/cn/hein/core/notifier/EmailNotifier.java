package cn.hein.core.notifier;

import cn.hein.common.entity.alarm.AlarmContent;
import cn.hein.common.entity.properties.PlatformProperties;
import cn.hein.common.enums.alarm.NotifyTypeEnum;
import cn.hein.common.equator.FieldInfo;
import cn.hein.core.context.DynamicTpContext;
import cn.hein.core.context.NotifyPlatformContext;
import cn.hein.core.executor.DynamicTpExecutor;
import cn.hein.core.service.EmailService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Implementation of a notifier strategy for email notifications.
 *
 * @author hein
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotifier implements Strategy {

    private final NotifyPlatformContext context;
    private final EmailService mailSender;
    private final Environment environment;
    private final DiscoveryClient discoveryClient;

    private static final List<String> executorFieldList = List.of(
            "corePoolSize",
            "maximumPoolSize",
            "queueType",
            "queueCapacity",
            "keepAliveTime",
            "timeUnit",
            "allowCoreThreadTimeOut",
            "rejectedExecutionHandler"
    );

    @Override
    public String name() {
        return NotifyTypeEnum.EMAIL.getDesc();
    }

    @Override
    public void alarmNotify(AlarmContent content, long timestamp, String sendKey) throws Exception {
        PlatformProperties platforms = context.getPlatforms(sendKey);
        if (platforms.isEnabled() && NotifyManager.canNotify(content, timestamp)) {
            mailSender.sendMailWithAttachment(platforms.getReceiver(),
                    "【报警】动态线程池运行告警",
                    "AlarmTemplate.ftl",
                    alarmBuildModel(content, platforms, timestamp), new ClassPathResource("images/email.png").getFile());
        }
    }

    @Override
    public void changeNotify(Map<String, List<FieldInfo>> changedFields, long timestamp, String sendKey) throws Exception {
        PlatformProperties platforms = context.getPlatforms(sendKey);
        if (platforms.isEnabled()) {
            mailSender.sendMailWithAttachment(platforms.getReceiver(),
                    "【通知】动态线程池参数变更",
                    "ChangeTemplate.ftl",
                    changeBuildModel(changedFields, platforms, timestamp), new ClassPathResource("images/email.png").getFile());
        }
    }

    private Map<String, Object> changeBuildModel(Map<String, List<FieldInfo>> changeFields, PlatformProperties platform, long timestamp) {
        Map<String, Object> model = new HashMap<>();
        model.put("title", "动态线程池运行告警");
        String applicationName = environment.getProperty("spring.application.name");
        model.put("serviceName", applicationName);
        ServiceInstance instance = discoveryClient.getInstances(applicationName)
                .stream().findFirst().orElseThrow(() -> new RuntimeException("no service."));
        model.put("instanceInfo", instance.getHost() + ":" + instance.getPort());
        model.put("environment", environment.getProperty("spring.profiles.active"));
        model.put("receiver", platform.getReceiver());
        model.put("changeTime", new Date(timestamp));

        // Thread Pool
        List<Map<String, Object>> changeList = new ArrayList<>();
        changeFields.forEach((threadPoolName, changedField) -> {
            Set<String> fieldSet = changedField.stream().map(FieldInfo::getFieldName).collect(Collectors.toSet());
            Map<String, Object> map = new HashMap<>();
            map.put("threadPoolName", threadPoolName);
            changedField.forEach(each -> {
                String fieldName = StrUtil.upperFirst(each.getFieldName());
                map.put("new" + fieldName, each.getValue1());
                map.put("old" + fieldName, each.getValue2());
            });
            DynamicTpExecutor executor = DynamicTpContext.getDynamicTp(threadPoolName);
            executorFieldList.stream().filter(field -> !fieldSet.contains(field)).forEach(field -> {
                switch (field) {
                    case "corePoolSize" -> {
                        int corePoolSize = executor.getCorePoolSize();
                        map.put("newCorePoolSize", corePoolSize);
                        map.put("oldCorePoolSize", corePoolSize);
                    }
                    case "maximumPoolSize" -> {
                        int maximumPoolSize = executor.getMaximumPoolSize();
                        map.put("newMaximumPoolSize", maximumPoolSize);
                        map.put("oldMaximumPoolSize", maximumPoolSize);
                    }
                    case "queueType" -> {
                        String simpleName = executor.getQueue().getClass().getSimpleName();
                        map.put("newQueueType", simpleName);
                        map.put("oldQueueType", simpleName);
                    }
                    case "queueCapacity" -> {
                        int capacity = executor.getQueue().size() + executor.getQueue().remainingCapacity();
                        map.put("newQueueCapacity", capacity);
                        map.put("oldQueueCapacity", capacity);
                    }
                    case "keepAliveTime" -> {
                        long keepAliveTime = executor.getKeepAliveTime();
                        map.put("newKeepAliveTime", keepAliveTime);
                        map.put("oldKeepAliveTime", keepAliveTime);
                    }
                    case "timeUnit" -> {
                        TimeUnit unit = executor.getUnit();
                        map.put("newTimeUnit", unit.toString());
                        map.put("oldTimeUnit", unit.toString());
                    }
                    case "allowCoreThreadTimeOut" -> {
                        boolean allowsCoreThreadTimeOut = executor.allowsCoreThreadTimeOut();
                        map.put("newAllowCoreThreadTimeOut", allowsCoreThreadTimeOut);
                        map.put("oldAllowCoreThreadTimeOut", allowsCoreThreadTimeOut);
                    }
                    case "rejectedExecutionHandler" -> {
                        String simpleName = executor.getRejectedExecutionHandler().getClass().getSimpleName();
                        map.put("newRejectedExecutionHandler", simpleName);
                        map.put("oldRejectedExecutionHandler", simpleName);
                    }
                }
            });
            changeList.add(map);
        });
        model.put("changeList", changeList);
        return model;
    }

    private Map<String, Object> alarmBuildModel(AlarmContent content, PlatformProperties platform, long timestamp) {
        DynamicTpExecutor executor = DynamicTpContext.getDynamicTp(content.getThreadPoolName());
        List<String> alarmTypes = content.getAlarmItems().stream().map(item -> item.getType().toString()).toList();
        Map<String, Object> model = new HashMap<>();
        model.put("title", "动态线程池运行告警");
        String applicationName = environment.getProperty("spring.application.name");
        model.put("serviceName", applicationName);
        ServiceInstance instance = discoveryClient.getInstances(applicationName)
                .stream().findFirst().orElseThrow(() -> new RuntimeException("no service."));
        model.put("instanceInfo", instance.getHost() + ":" + instance.getPort());
        model.put("environment", environment.getProperty("spring.profiles.active"));
        model.put("threadPoolName", content.getThreadPoolName());
        model.put("alarmItemTypes", String.join(", ", alarmTypes));
        model.put("alarmItemList", content.getAlarmItems());
        model.put("receiver", platform.getReceiver());
        model.put("alarmTime", new Date(timestamp));

        // Thread Pool
        model.put("corePoolSize", executor.getCorePoolSize());
        model.put("maximumPoolSize", executor.getMaximumPoolSize());
        model.put("poolSize", executor.getPoolSize());
        model.put("activeCount", executor.getActiveCount());

        // BlockingQueue
        model.put("queueType", executor.getQueue().getClass().getName());
        model.put("queueCapacity", executor.getQueue().size() + executor.getQueue().remainingCapacity());
        model.put("queueSize", executor.getQueue().size());
        model.put("remainingCapacity", executor.getQueue().remainingCapacity());

        // Rejected Count
        model.put("rejectedExecutionHandler", executor.getOriginalHandlerType());
        model.put("rejectedTotalCount", executor.getRejectedCount());
        return model;
    }
}