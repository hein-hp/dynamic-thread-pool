package cn.hein.common.enums.executors;

import cn.hein.common.queue.ResizeLinkedBlockingQueue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.*;

/**
 * Enumeration class for blocking queue types.
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
     * The name of the blocking queue.
     */
    private final String name;

    /**
     * The class type of the blocking queue.
     */
    @SuppressWarnings("rawtypes")
    private final Class<? extends BlockingQueue> queueClass;

    /**
     * Retrieves a blocking queue instance based on the provided name and capacity.
     *
     * @param name     The name identifying the queue type.
     * @param capacity The initial capacity of the queue.
     * @return An instance of the specified blocking queue.
     * @throws IllegalArgumentException If the queue type is unknown.
     */
    public static BlockingQueue<Runnable> getBlockingQueue(String name, int capacity) {
        for (BlockingQueueTypeEnum type : BlockingQueueTypeEnum.values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return createQueue(type.queueClass, capacity);
            }
        }
        throw new IllegalArgumentException("Unknown blocking queue type: " + name);
    }

    /**
     * Creates an instance of the blocking queue using reflection based on the provided class type and capacity.
     *
     * @param queueClass The class type of the queue to be instantiated.
     * @param capacity   The capacity for the queue.
     * @return An instance of the blocking queue.
     * @throws IllegalArgumentException If the queue type is unsupported.
     */
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

    /**
     * Retrieves the class associated with the given queue name.
     *
     * @param name The name of the queue.
     * @return The Class object representing the queue.
     * @throws IllegalArgumentException If the queue type is unknown.
     */
    @SuppressWarnings("rawtypes")
    public static Class<? extends BlockingQueue> getClassByName(String name) {
        for (BlockingQueueTypeEnum type : BlockingQueueTypeEnum.values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type.queueClass;
            }
        }
        throw new IllegalArgumentException("Unknown blocking queue type: " + name);
    }
}