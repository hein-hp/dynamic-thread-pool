package cn.hein.core.publisher;

import cn.hein.common.entity.info.DynamicTpCollectInfo;
import cn.hein.core.event.DynamicTpCollectCompleteEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Publishes a Dynamic ThreadPool collect event upon collect task complete.
 *
 * @author hein
 */
@Slf4j
@Component
public class DynamicTpCollectCompleteEventPublisher extends AbstractEventPublisher {

    public DynamicTpCollectCompleteEventPublisher(ApplicationEventPublisher publisher) {
        super(publisher);
    }

    @Override
    protected ApplicationEvent buildEvent(Object source) {
        return new DynamicTpCollectCompleteEvent(this, (DynamicTpCollectInfo) source);
    }
}
