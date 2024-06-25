package cn.hein.core.dynamic;

import cn.hein.core.common.enums.dynamic.BlockingQueueTypeEnum;
import cn.hein.core.common.enums.dynamic.RejectionPolicyTypeEnum;
import cn.hein.core.config.DynamicThreadPoolProperties;
import cn.hein.core.config.ExecutorProperties;
import cn.hein.core.toolkit.TimeUnitConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置类
 *
 * @author hein
 */
@Configuration
@RequiredArgsConstructor
public class ThreadPoolConfig {

    private final DynamicThreadPoolProperties properties;

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        if (!properties.isEnable()) {
            return new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(1000),
                    new ThreadPoolExecutor.AbortPolicy());
        }
        ExecutorProperties executorProperties = properties.getExecutorProperties().get(0);
        return new DynamicThreadPoolExecutor(
                executorProperties.getExecutorNamePrefix(),
                executorProperties.getCorePoolSize(),
                executorProperties.getMaximumPoolSize(),
                executorProperties.getKeepAliveSeconds(),
                TimeUnitConverter.convert(executorProperties.getTimeUnit()),
                BlockingQueueTypeEnum.getBlockingQueue(executorProperties.getQueueType(), executorProperties.getQueueCapacity()),
                RejectionPolicyTypeEnum.getRejectionPolicy(executorProperties.getRejectedExecutionHandler())
        );
    }
}
