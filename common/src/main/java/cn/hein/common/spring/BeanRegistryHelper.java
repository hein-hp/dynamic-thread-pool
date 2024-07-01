package cn.hein.common.spring;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class for managing bean registration in a Spring context.
 *
 * @author hein
 */
@Slf4j
public final class BeanRegistryHelper {

    private BeanRegistryHelper() {}

    /**
     * Registers a bean definition with the given name, class, properties, and constructor arguments.
     * Overwrites an existing bean definition if present.
     *
     * @param registry        the BeanDefinitionRegistry to register the bean to
     * @param beanName        the name of the bean
     * @param clazz           the class of the bean
     * @param propertyValues  a map of property names to values
     * @param constructorArgs constructor arguments for the bean
     */
    public static void register(BeanDefinitionRegistry registry,
                                String beanName,
                                Class<?> clazz,
                                Map<String, Object> propertyValues,
                                Object... constructorArgs) {
        register(registry, beanName, clazz, propertyValues, null, constructorArgs);
    }

    /**
     * Registers a bean definition with dependencies.
     *
     * @param registry           the BeanDefinitionRegistry to register the bean to
     * @param beanName           the name of the bean
     * @param clazz              the class of the bean
     * @param propertyValues     a map of property names to values
     * @param dependsOnBeanNames a list of bean names that this bean depends on
     * @param constructorArgs    constructor arguments for the bean
     */
    public static void register(BeanDefinitionRegistry registry,
                                String beanName,
                                Class<?> clazz,
                                Map<String, Object> propertyValues,
                                List<String> dependsOnBeanNames,
                                Object... constructorArgs) {
        if (isPresent(registry, beanName, clazz)) {
            log.info("Bean [{}] already exists and will be overwritten.", beanName);
            registry.removeBeanDefinition(beanName);
        }
        doRegister(registry, beanName, clazz, propertyValues, dependsOnBeanNames, constructorArgs);
    }

    /**
     * Registers a bean definition if no existing definition is found.
     *
     * @param registry        the BeanDefinitionRegistry to register the bean to
     * @param beanName        the name of the bean
     * @param clazz           the class of the bean
     * @param constructorArgs constructor arguments for the bean
     */
    public static void registerIfAbsent(BeanDefinitionRegistry registry,
                                        String beanName,
                                        Class<?> clazz,
                                        Object... constructorArgs) {
        registerIfAbsent(registry, beanName, clazz, null, null, constructorArgs);
    }

    /**
     * Registers a bean definition with properties if no existing definition is found.
     *
     * @param registry        the BeanDefinitionRegistry to register the bean to
     * @param beanName        the name of the bean
     * @param clazz           the class of the bean
     * @param propertyValues  a map of property names to values
     * @param constructorArgs constructor arguments for the bean
     */
    public static void registerIfAbsent(BeanDefinitionRegistry registry,
                                        String beanName,
                                        Class<?> clazz,
                                        Map<String, Object> propertyValues,
                                        Object... constructorArgs) {
        registerIfAbsent(registry, beanName, clazz, propertyValues, null, constructorArgs);
    }

    /**
     * Registers a bean definition with properties and dependencies if no existing definition is found.
     *
     * @param registry           the BeanDefinitionRegistry to register the bean to
     * @param beanName           the name of the bean
     * @param clazz              the class of the bean
     * @param propertyValues     a map of property names to values
     * @param dependsOnBeanNames a list of bean names that this bean depends on
     * @param constructorArgs    constructor arguments for the bean
     */
    public static void registerIfAbsent(BeanDefinitionRegistry registry,
                                        String beanName,
                                        Class<?> clazz,
                                        Map<String, Object> propertyValues,
                                        List<String> dependsOnBeanNames,
                                        Object... constructorArgs) {
        if (!isPresent(registry, beanName, clazz)) {
            doRegister(registry, beanName, clazz, propertyValues, dependsOnBeanNames, constructorArgs);
        }
    }

    /**
     * Checks if a bean definition with the given name and class is already present.
     *
     * @param registry the BeanDefinitionRegistry to check
     * @param beanName the name of the bean
     * @param clazz    the class of the bean
     * @return true if the bean definition is present, false otherwise
     */
    public static boolean isPresent(BeanDefinitionRegistry registry, String beanName, Class<?> clazz) {
        return getBeanNames((ListableBeanFactory) registry, clazz).contains(beanName);
    }

    /**
     * Retrieves a set of bean names for beans of the given class.
     *
     * @param beanFactory the ListableBeanFactory to retrieve bean names from
     * @param clazz       the class of the beans to retrieve names for
     * @return a set of bean names
     */
    public static Set<String> getBeanNames(ListableBeanFactory beanFactory, Class<?> clazz) {
        return Arrays.stream(beanFactory.getBeanNamesForType(clazz, true, false))
                .collect(Collectors.toSet());
    }

    /**
     * Performs the actual registration of a bean definition.
     *
     * @param registry           the BeanDefinitionRegistry to register the bean to
     * @param beanName           the name of the bean
     * @param clazz              the class of the bean
     * @param propertyValues     a map of property names to values
     * @param dependsOnBeanNames a list of bean names that this bean depends on
     * @param constructorArgs    constructor arguments for the bean
     */
    private static void doRegister(BeanDefinitionRegistry registry,
                                   String beanName,
                                   Class<?> clazz,
                                   Map<String, Object> propertyValues,
                                   List<String> dependsOnBeanNames,
                                   Object... constructorArgs) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        for (Object arg : constructorArgs) {
            builder.addConstructorArgValue(arg);
        }
        if (MapUtil.isNotEmpty(propertyValues)) {
            propertyValues.forEach(builder::addPropertyValue);
        }
        if (CollUtil.isNotEmpty(dependsOnBeanNames)) {
            dependsOnBeanNames.forEach(builder::addDependsOn);
        }
        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }
}