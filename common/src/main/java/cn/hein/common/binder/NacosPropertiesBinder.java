package cn.hein.common.binder;

import cn.hein.common.constant.executor.NacosConstant;
import cn.hein.common.entity.properties.DynamicTpProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * Implementation of PropertiesBinder that binds properties using Spring's Binder.
 * It supports binding from both a Map of properties and a Spring Environment.
 *
 * @author hein
 */
public class NacosPropertiesBinder implements PropertiesBinder {

    @Override
    public void bindProperties(Map<?, Object> properties, DynamicTpProperties bindProp) {
        doBind(bindProp, new Binder(new MapConfigurationPropertySource(properties)));
    }

    @Override
    public void bindProperties(Environment environment, DynamicTpProperties bindProp) {
        doBind(bindProp, Binder.get(environment));
    }

    /**
     * Performs the actual binding operation using the provided Binder.
     *
     * @param bindProp The DynamicTpProperties instance to bind the properties to.
     * @param binder   The Binder instance to use for binding.
     */
    private void doBind(DynamicTpProperties bindProp, Binder binder) {
        binder.bind(NacosConstant.MAIN_PREFIX,
                Bindable.of(ResolvableType.forClass(DynamicTpProperties.class))
                        .withExistingValue(bindProp));
    }
}