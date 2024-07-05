package cn.hein.core.event;

import org.springframework.context.ApplicationEvent;

/**
 * The Refresh Event
 *
 * @author hein
 */
public class RefreshEvent extends ApplicationEvent {

    public RefreshEvent(Object source) {
        super(source);
    }
}
