package cn.hein.core.event;

import org.springframework.context.ApplicationEvent;

/**
 * The Notify Event
 *
 * @author hein
 */
public class NotifyEvent extends ApplicationEvent {

    public NotifyEvent(Object source) {
        super(source);
    }
}
