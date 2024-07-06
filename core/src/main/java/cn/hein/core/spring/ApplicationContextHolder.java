package cn.hein.core.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Singleton holder for Spring's ApplicationContext to provide easy access to beans.
 *
 * @author hein
 */
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;

    /**
     * Sets the ApplicationContext for this holder.
     *
     * @param context The ApplicationContext to be set.
     * @throws BeansException if there is an issue setting the context.
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext context) throws BeansException {
        ApplicationContextHolder.context = context;
    }

    /**
     * Retrieves a bean from the ApplicationContext by its type.
     *
     * @param <T>   the type of the bean to retrieve.
     * @param clazz the class object representing the bean type.
     * @return the bean instance.
     */
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    /**
     * Retrieves a bean from the ApplicationContext by its name.
     *
     * @param name the name of the bean to retrieve.
     * @return the bean instance.
     */
    public static Object getBean(String name) {
        return context.getBean(name);
    }

    /**
     * Retrieves a bean from the ApplicationContext by its name and type.
     *
     * @param <T>   the type of the bean to retrieve.
     * @param name  the name of the bean.
     * @param clazz the class object representing the bean type.
     * @return the bean instance.
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return context.getBean(name, clazz);
    }

    /**
     * Retrieves all beans of a specific type from the ApplicationContext.
     *
     * @param <T>   the type of the beans to retrieve.
     * @param clazz the class object representing the bean type.
     * @return a map of bean names to bean instances.
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return context.getBeansOfType(clazz);
    }

    /**
     * Finds if a bean has a specific annotation.
     *
     * @param <A>            the type of the annotation to search for.
     * @param beanName       the name of the bean.
     * @param annotationType the class object representing the annotation type.
     * @return the annotation if found, otherwise null.
     */
    public static <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) {
        return context.findAnnotationOnBean(beanName, annotationType);
    }

    /**
     * Checks if a bean with the given name exists in the ApplicationContext.
     *
     * @param name the name of the bean.
     * @return true if the bean exists, false otherwise.
     */
    public static boolean containsBean(String name) {
        return context.containsBean(name);
    }

    /**
     * Retrieves the ApplicationContext instance.
     *
     * @return the ApplicationContext.
     */
    public static ApplicationContext getInstance() {
        return context;
    }
}
