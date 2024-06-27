package cn.hein.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 动态线程池配置类
 *
 * @author hein
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "dynamic.thread-pool")
public class DynamicThreadPoolProperties {

    /**
     * 是否开启动态线程池
     */
    private boolean enable;

    /**
     * 告警平台
     */
    private List<PlatformProperties> platforms;

    /**
     * 动态线程池配置
     */
    private List<ExecutorProperties> executors;

    /**
     * 日志输出
     */
    private LogProperties log;
}
