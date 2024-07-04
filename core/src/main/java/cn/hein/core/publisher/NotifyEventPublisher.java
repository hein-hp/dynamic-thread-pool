package cn.hein.core.publisher;

import cn.hein.core.event.NotifyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Publisher for NotifyEvents.
 */
@Component
public class NotifyEventPublisher extends AbstractEventPublisher {

    public NotifyEventPublisher(ApplicationEventPublisher publisher) {
        super(publisher);
    }

    @Override
    protected ApplicationEvent buildEvent(Object source) {
        return new NotifyEvent(source);
    }
}