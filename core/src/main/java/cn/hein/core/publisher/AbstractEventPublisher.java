package cn.hein.core.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Abstract base class for event publishers that utilize Spring's ApplicationEventPublisher.
 *
 * @author hein
 */
@RequiredArgsConstructor
public abstract class AbstractEventPublisher implements EventPublisher {

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
    protected abstract ApplicationEvent buildEvent(final Object source);

    /**
     * Publishes an event by first building it from the given source object, then publishing it.
     *
     * @param source The source object from which to build and publish the event.
     */
    public void publishEvent(final Object source) {
        beforePub();
        publisher.publishEvent(buildEvent(source));
        afterPub();
    }

    /**
     * Hook method called before the event is published.
     */
    protected void beforePub() {
        // Default implementation does nothing.
    }

    /**
     * Hook method called after the event has been published.
     */
    protected void afterPub() {
        // Default implementation does nothing.
    }
}