package cn.hein.core.notifier;

import cn.hein.common.entity.alarm.AlarmContent;
import cn.hein.common.enums.alarm.NotifyTypeEnum;
import org.springframework.stereotype.Component;

/**
 * Implementation of a notifier strategy for email notifications.
 */
@Component
public class EmailNotifier implements Strategy {

    @Override
    public String name() {
        return NotifyTypeEnum.EMAIL.getDesc();
    }

    @Override
    public void notify(AlarmContent content, long timestamp) {
        // @TODO email sending
    }
}