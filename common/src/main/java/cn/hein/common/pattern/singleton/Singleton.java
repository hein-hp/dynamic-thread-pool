package cn.hein.common.pattern.singleton;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton pattern implementation that manages a registry of singleton objects.
 *
 * @author hein
 */
public enum Singleton {

    INSTANCE;

    private static final Map<Class<?>, Object> SINGLETON = new ConcurrentHashMap<>(64);

    /**
     * Registers a singleton instance for the given class.
     *
     * @param clazz    The class for which to register the singleton.
     * @param instance The singleton instance to register.
     */
    public void singleton(final Class<?> clazz, final Object instance) {
        SINGLETON.put(clazz, instance);
    }

    /**
     * Retrieves the singleton instance registered for the given class.
     *
     * @param clazz The class for which to retrieve the singleton.
     * @param <T>   The type of the singleton instance.
     * @return The singleton instance, or null if none is registered.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(final Class<T> clazz) {
        return (T) SINGLETON.get(clazz);
    }
}