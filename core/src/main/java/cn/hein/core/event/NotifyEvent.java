package cn.hein.core.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * The Notify Event
 *
 * @author hein
 */
@Getter
public class NotifyEvent extends ApplicationEvent {

    public NotifyEvent(Object source) {
        super(source);
    }
}
