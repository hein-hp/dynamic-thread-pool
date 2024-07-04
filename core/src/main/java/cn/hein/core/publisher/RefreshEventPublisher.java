package cn.hein.core.publisher;

import cn.hein.core.event.RefreshEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Publisher for RefreshEvents.
 *
 * @author hein
 */
@Component
public class RefreshEventPublisher extends AbstractEventPublisher {

    public RefreshEventPublisher(ApplicationEventPublisher publisher) {
        super(publisher);
    }

    @Override
    protected ApplicationEvent buildEvent(Object source) {
        return new RefreshEvent(source);
    }
}
