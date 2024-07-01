package cn.hein.core.executor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Custom thread factory that names threads with a given prefix.
 *
 * @author hein
 */
public class NamedThreadFactory implements ThreadFactory {

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
