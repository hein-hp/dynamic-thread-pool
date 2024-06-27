package cn.hein.core.dynamic;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A dynamically configurable ThreadPoolExecutor.
 *
 * @author hein
 */
public class DynamicTpExecutor extends ThreadPoolExecutor {

    public DynamicTpExecutor(
            String executorNamePrefix,
            int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new NamedThreadFactory(executorNamePrefix, 0L), handler);
    }

    /**
     * Custom thread factory that names threads with a given prefix.
     */
    public static class NamedThreadFactory implements ThreadFactory {

        /**
         * Prefix for the thread's name.
         */
        private final String prefix;

        /**
         * Counter for generating unique thread IDs.
         */
        private final AtomicLong id;

        public NamedThreadFactory(String prefix, Long startId) {
            this.prefix = prefix;
            id = new AtomicLong(startId);
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName(prefix + "-" + id.getAndDecrement());
            return thread;
        }
    }
}