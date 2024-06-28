package cn.hein.core.refresher;

import cn.hein.core.publisher.DynamicTpRefreshEventPublisher;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * NacosRefresher listens for configuration changes in Nacos and triggers a refresh of dynamic thread pools.
 * It implements InitializingBean to ensure that the listener is registered after all necessary beans are initialized.
 *
 * @author hein
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NacosRefresher implements InitializingBean {

    private final NacosConfigManager nacosConfigManager;
    private final DynamicTpRefreshEventPublisher publisher;

    @Value("${spring.cloud.nacos.config.name}")
    private String dataId; // Nacos configuration file ID

    @Value("${spring.cloud.nacos.config.group}")
    private String group; // Nacos configuration group

    @Override
    public void afterPropertiesSet() throws NacosException {
        log.info("Registering Nacos configuration listener for dataId: {}, group: {}", dataId, group);
        nacosConfigManager.getConfigService().addListener(dataId, group, new Listener() {
            @Override
            public Executor getExecutor() {
                // Returns a fixed-size thread pool for handling configuration update events
                return Executors.newSingleThreadExecutor();
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                log.info("Received Nacos configuration update: {}", configInfo);
                // When the configuration changes, publish a Dynamic ThreadPool refresh event
                publisher.publishEvent(configInfo);
            }
        });
    }
}