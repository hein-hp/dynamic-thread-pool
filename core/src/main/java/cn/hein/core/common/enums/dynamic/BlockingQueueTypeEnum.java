package cn.hein.core.common.enums.dynamic;

import cn.hein.core.queue.ResizeLinkedBlockingQueue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.*;

/**
 * 阻塞队列枚举类
 *
 * @author hein
 */
@Getter
@AllArgsConstructor
public enum BlockingQueueTypeEnum {

    /**
     * ArrayBlockingQueue
     */
    ARRAY_BLOCKING_QUEUE("ArrayBlockingQueue", ArrayBlockingQueue.class),

    /**
     * LinkedBlockingQueue
     */
    LINKED_BLOCKING_QUEUE("LinkedBlockingQueue", LinkedBlockingQueue.class),

    /**
     * PriorityBlockingQueue
     */
    PRIORITY_BLOCKING_QUEUE("PriorityBlockingQueue", PriorityBlockingQueue.class),

    /**
     * SynchronousQueue
     */
    SYNCHRONOUS_QUEUE("SynchronousQueue", SynchronousQueue.class),

    /**
     * ResizeLinkedBlockingQueue
     */
    RESIZE_LINKED_BLOCKING_QUEUE("ResizeLinkedBlockingQueue", ResizeLinkedBlockingQueue.class);

    /**
     * 阻塞队列名称
     */
    private final String name;

    /**
     * 阻塞队列类型
     */
    @SuppressWarnings("rawtypes")
    private final Class<? extends BlockingQueue> queueClass;

    public static BlockingQueue<Runnable> getBlockingQueue(String name, int capacity) {
        for (BlockingQueueTypeEnum type : BlockingQueueTypeEnum.values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return createQueue(type.queueClass, capacity);
            }
        }
        throw new IllegalArgumentException("Unknown blocking queue type: " + name);
    }

    @SuppressWarnings("rawtypes")
    private static BlockingQueue<Runnable> createQueue(Class<? extends BlockingQueue> queueClass, int capacity) {
        try {
            if (queueClass.equals(ArrayBlockingQueue.class)) {
                return new ArrayBlockingQueue<>(capacity);
            } else if (queueClass.equals(LinkedBlockingQueue.class)) {
                return new LinkedBlockingQueue<>(capacity);
            } else if (queueClass.equals(PriorityBlockingQueue.class)) {
                return new PriorityBlockingQueue<>(capacity);
            } else if (queueClass.equals(SynchronousQueue.class)) {
                return new SynchronousQueue<>();
            } else if (queueClass.equals(ResizeLinkedBlockingQueue.class)) {
                return new ResizeLinkedBlockingQueue<>(capacity);
            } else {
                throw new IllegalArgumentException("Unsupported blocking queue type: " + queueClass.getName());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create blocking queue", e);
        }
    }
}
