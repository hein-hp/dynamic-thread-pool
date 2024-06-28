package cn.hein.core.publisher;

import cn.hein.core.collector.DynamicTpCollector;
import cn.hein.core.event.NacosConfigRefreshEvent;
import cn.hein.core.properties.DynamicTpProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import static cn.hein.common.constant.executor.NacosConstant.DYNAMIC;
import static cn.hein.common.constant.executor.NacosConstant.THREAD_POOL;
import static cn.hein.common.toolkit.StringUtil.kebabCaseToCamelCase;
import static cn.hein.common.toolkit.YamlUtil.convertWithoutPrefix;

/**
 * Publishes a Dynamic ThreadPool refresh event upon configuration changes in Nacos.
 *
 * @author hein
 */
@Slf4j
@Component
public class DynamicTpRefreshEventPublisher extends AbstractEventPublisher {

    private final DynamicTpCollector collector;

    public DynamicTpRefreshEventPublisher(ApplicationEventPublisher publisher, DynamicTpCollector collector) {
        super(publisher);
        this.collector = collector;
    }

    @Override
    protected ApplicationEvent buildEvent(Object source) {
        DynamicTpProperties message =
                convertWithoutPrefix((String) source, DynamicTpProperties.class, DYNAMIC, THREAD_POOL);
        message.getExecutors().forEach(executor ->
                executor.setBeanName(kebabCaseToCamelCase(executor.getThreadPoolName())));
        return new NacosConfigRefreshEvent(this, message);
    }

    @Override
    protected void beforePub() {
        collector.stop();
    }
}