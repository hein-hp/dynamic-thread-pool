package cn.hein.core.context;

import cn.hein.common.entity.properties.DynamicTpProperties;
import cn.hein.common.entity.properties.PlatformProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Context manager for notify platforms.
 *
 * @author hein
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotifyPlatformContext {

    private static final Map<String, PlatformProperties> PLATFORM_CONTEXT = new ConcurrentHashMap<>();
    private final Lock lock = new ReentrantLock();

    private final DynamicTpProperties prop;

    /**
     * Get PlatformProperties by key.
     *
     * @param key the key
     * @return PlatformProperties
     */
    public PlatformProperties getPlatforms(String key) {
        lock.lock();
        try {
            return Optional.of(PLATFORM_CONTEXT.get(key))
                    .orElseThrow(() -> new RuntimeException("no platform found."));
        } finally {
            lock.unlock();
        }
    }

    /**
     * Refresh PlatformProperties. Just clear and reload
     */
    public void refresh() {
        lock.lock();
        try {
            // clear and reload
            clear();
            reload();
            log.info("PlatformProperties refresh");
        } finally {
            lock.unlock();
        }
    }

    private void reload() {
        prop.getPlatforms().forEach(this::registerPlatForm);
    }

    private void registerPlatForm(PlatformProperties prop) {
        PLATFORM_CONTEXT.putIfAbsent(prop.getKey(), prop);
    }

    private void clear() {
        PLATFORM_CONTEXT.clear();
    }
}
