package cn.hein.core.listener;

import cn.hein.core.event.DynamicTpCollectCompleteEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Listener responsible for handle DynamicTpCollectCompleteEvent.
 *
 * @author hein
 */
@Component
public class DynamicTpCollectCompleteListener implements ApplicationListener<DynamicTpCollectCompleteEvent> {

    @Override
    public void onApplicationEvent(DynamicTpCollectCompleteEvent event) {
        // TODO handle event
        System.out.println("event.getCollectInfo() = " + event.getCollectInfo());
    }
}
