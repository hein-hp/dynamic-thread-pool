package cn.hein.core.event;

import cn.hein.common.entity.info.DynamicTpCollectInfo;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Event triggered when a collection task for dynamic thread pool metrics completes.
 *
 * @author Hein
 */
@Getter
public class DynamicTpCollectCompleteEvent extends ApplicationEvent {

    private final DynamicTpCollectInfo collectInfo;

    /**
     * Constructs a new DynamicTpCollectCompleteEvent.
     *
     * @param source the object on which the event initially occurred or with which the event is associated
     * @param collectInfo the collected information about the dynamic thread pool
     */
    public DynamicTpCollectCompleteEvent(Object source, DynamicTpCollectInfo collectInfo) {
        super(source);
        this.collectInfo = collectInfo;
    }
}