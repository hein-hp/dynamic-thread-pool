package cn.hein.core.support;

import cn.hein.common.binder.NacosPropertiesBinder;
import cn.hein.common.binder.PropertiesBinder;
import cn.hein.common.entity.properties.DynamicTpProperties;
import cn.hein.common.pattern.singleton.Singleton;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.Optional;

/**
 * Helper class for binding properties using the selected properties binder.
 *
 * @author hein
 */
public final class PropertiesBinderHelper {

    private PropertiesBinderHelper() {}

    /**
     * Retrieves the singleton instance of the selected PropertiesBinder.
     *
     * @return An instance of PropertiesBinder.
     */
    private static PropertiesBinder getBinder() {
        return Optional.ofNullable(Singleton.INSTANCE.get(PropertiesBinder.class))
                .orElseGet(() -> {
                    PropertiesBinder selected = new NacosPropertiesBinder();
                    Singleton.INSTANCE.singleton(PropertiesBinder.class, selected);
                    return selected;
                });
    }

    /**
     * Binds properties from a map to a DynamicTpProperties instance.
     *
     * @param properties A map containing the properties to be bound.
     * @param prop       The DynamicTpProperties instance to bind the properties to.
     */
    public static void bindProperties(Map<?, Object> properties, DynamicTpProperties prop) {
        getBinder().bindProperties(properties, prop);
    }

    /**
     * Binds properties from an Environment to a DynamicTpProperties instance.
     *
     * @param environment The Environment from which to retrieve properties.
     * @param prop        The DynamicTpProperties instance to bind the properties to.
     */
    public static void bindProperties(Environment environment, DynamicTpProperties prop) {
        getBinder().bindProperties(environment, prop);
    }
}