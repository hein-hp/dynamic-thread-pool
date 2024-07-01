package cn.hein.core.publisher;

/**
 * Defines the contract for an event publisher.
 *
 * @author hein
 */
public interface EventPublisher {

    /**
     * Publishes an event to all registered listeners.
     *
     * @param source The source object from which the event originated.
     */
    void publishEvent(final Object source);
}