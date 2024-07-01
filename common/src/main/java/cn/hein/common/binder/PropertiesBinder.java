package cn.hein.common.binder;

import cn.hein.common.entity.properties.DynamicTpProperties;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * Interface for binding properties to a DynamicTpProperties object.
 *
 * @author hein
 */
public interface PropertiesBinder {

    /**
     * Binds properties from a map to a DynamicTpProperties instance.
     *
     * @param properties The map containing the properties to be bound.
     * @param bindProp   The DynamicTpProperties instance to bind the properties to.
     */
    void bindProperties(Map<?, Object> properties, DynamicTpProperties bindProp);

    /**
     * Binds properties from a Spring Environment to a DynamicTpProperties instance.
     *
     * @param environment The Spring Environment from which to retrieve properties.
     * @param bindProp    The DynamicTpProperties instance to bind the properties to.
     */
    void bindProperties(Environment environment, DynamicTpProperties bindProp);
}