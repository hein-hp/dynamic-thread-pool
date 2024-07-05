package cn.hein.core.notifier;

import cn.hein.common.entity.alarm.AlarmContent;
import cn.hein.common.entity.properties.PlatformProperties;
import cn.hein.common.enums.alarm.NotifyTypeEnum;
import cn.hein.core.context.DynamicTpContext;
import cn.hein.core.context.NotifyPlatformContext;
import cn.hein.core.executor.DynamicTpExecutor;
import cn.hein.core.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public String name() {
        return NotifyTypeEnum.EMAIL.getDesc();
    }

    @Override
    public void notify(AlarmContent content, long timestamp, String sendKey) throws Exception {
        PlatformProperties platforms = context.getPlatforms(sendKey);
        if (platforms.isEnabled() && NotifyManager.canNotify(content, timestamp)) {
            mailSender.sendMailWithAttachment(platforms.getReceiver(),
                    "【报警】动态线程池运行告警",
                    "AlarmTemplate.ftl",
                    buildModel(content, platforms, timestamp), new ClassPathResource("images/email.png").getFile());
        }
    }

    private Map<String, Object> buildModel(AlarmContent content, PlatformProperties platform, long timestamp) {
        DynamicTpExecutor executor = DynamicTpContext.getDynamicTp(content.getThreadPoolName());
        List<String> alarmTypes = content.getAlarmItems().stream().map(item -> item.getType().toString()).toList();
        Map<String, Object> model = new HashMap<>();
        model.put("title", "动态线程池运行告警");
        model.put("serviceName", environment.getProperty("spring.application.name"));
        model.put("instanceInfo", "192.168.88.100:8080");
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
        return model;
    }
}