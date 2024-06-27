package cn.hein.core.dynamic;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 动态线程池
 *
 * @author hein
 */
public class DynamicThreadPoolExecutor extends ThreadPoolExecutor {

    public DynamicThreadPoolExecutor(
            String executorNamePrefix,
            int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new NamedThreadFactory(executorNamePrefix), handler);
    }

    /**
     * 命名线程工厂
     */
    private static class NamedThreadFactory implements ThreadFactory {

        /**
         * 线程名称前缀
         */
        private final String prefix;

        /**
         * 计数器
         */
        private final AtomicLong id = new AtomicLong(0);

        public NamedThreadFactory(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName(prefix + "-" + id.getAndDecrement());
            return thread;
        }
    }
}
