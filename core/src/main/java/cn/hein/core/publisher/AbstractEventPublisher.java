package cn.hein.core.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Abstract base class for event publishers that utilize Spring's ApplicationEventPublisher.
 * This class provides an abstract method to construct an ApplicationEvent from a given source object,
 * and a method to publish the constructed event using the provided ApplicationEventPublisher.
 *
 * @author hein
 */
@RequiredArgsConstructor
public abstract class AbstractEventPublisher {

    /**
     * The ApplicationEventPublisher used to publish events.
     */
    private final ApplicationEventPublisher publisher;

    /**
     * Builds an ApplicationEvent from the given source object.
     *
     * @param source The source object from which to build the event.
     * @return The constructed ApplicationEvent.
     */
    protected abstract ApplicationEvent buildEvent(Object source);

    /**
     * Publishes an event by first building it from the given source object, then publishing it.
     *
     * @param source The source object from which to build and publish the event.
     */
    public void publishEvent(Object source) {
        publisher.publishEvent(buildEvent(source));
    }
}