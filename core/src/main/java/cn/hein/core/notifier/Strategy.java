package cn.hein.core.notifier;

/**
 * Represents a strategy for handling specific types of notifications.
 *
 * @author hein
 */
public interface Strategy extends Notifier {

    /**
     * Returns the name of the strategy.
     *
     * @return a string representing the name of the strategy
     */
    String name();
}