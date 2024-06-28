package cn.hein.core.publisher;

import cn.hein.core.event.DynamicTpRefreshEvent;
import cn.hein.core.properties.DynamicTpProperties;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static cn.hein.common.constant.executor.NacosConfigPrefixConstant.DYNAMIC;
import static cn.hein.common.constant.executor.NacosConfigPrefixConstant.THREAD_POOL;
import static cn.hein.common.toolkit.StringUtil.kebabCaseToCamelCase;
import static cn.hein.common.toolkit.YamlUtil.convertWithoutPrefix;

/**
 * Publishes a Dynamic ThreadPool refresh event upon configuration changes in Nacos.
 *
 * @author hein
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicTpRefreshEventPublisher implements InitializingBean {

    private final NacosConfigManager nacosConfigManager;
    private final ApplicationEventPublisher publisher;

    @Value("${spring.cloud.nacos.config.name}")
    private String dataId; // Nacos configuration file ID

    @Value("${spring.cloud.nacos.config.group}")
    private String group; // Nacos configuration group

    @Override
    public void afterPropertiesSet() throws Exception {
        nacosConfigManager.getConfigService().addListener(dataId, group, new Listener() {
            @Override
            public Executor getExecutor() {
                // Returns a fixed-size thread pool for handling configuration update events
                return Executors.newFixedThreadPool(1);
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                // When the configuration changes, publish a Dynamic ThreadPool refresh event
                DynamicTpProperties message = convertWithoutPrefix(configInfo, DynamicTpProperties.class, DYNAMIC, THREAD_POOL);
                message.getExecutors().forEach(executor -> executor.setBeanName(kebabCaseToCamelCase(executor.getThreadPoolName())));
                publisher.publishEvent(new DynamicTpRefreshEvent(this, message));
            }
        });
    }
}