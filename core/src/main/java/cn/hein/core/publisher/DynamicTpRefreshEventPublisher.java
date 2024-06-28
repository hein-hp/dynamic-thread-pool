package cn.hein.core.publisher;

import cn.hein.common.spring.ApplicationContextHolder;
import cn.hein.common.toolkit.ThreadPoolInfoPrinter;
import cn.hein.core.event.DynamicTpRefreshEvent;
import cn.hein.core.executor.DynamicTpExecutor;
import cn.hein.core.properties.DynamicTpProperties;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
public class DynamicTpRefreshEventPublisher extends AbstractEventPublisher implements InitializingBean {

    private final NacosConfigManager nacosConfigManager;

    @Value("${spring.cloud.nacos.config.name}")
    private String dataId; // Nacos configuration file ID

    @Value("${spring.cloud.nacos.config.group}")
    private String group; // Nacos configuration group

    public DynamicTpRefreshEventPublisher(ApplicationEventPublisher publisher, NacosConfigManager nacosConfigManager) {
        super(publisher);
        this.nacosConfigManager = nacosConfigManager;
    }

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
                System.out.println("before");
                DynamicTpExecutor bean = ApplicationContextHolder.getBean(DynamicTpExecutor.class);
                ThreadPoolInfoPrinter.printThreadPoolInfo(bean);
                publishEvent(message);
                System.out.println("after");
                ThreadPoolInfoPrinter.printThreadPoolInfo(bean);
            }
        });
    }

    @Override
    protected ApplicationEvent buildEvent(Object source) {
        return new DynamicTpRefreshEvent(this, (DynamicTpProperties) source);
    }
}