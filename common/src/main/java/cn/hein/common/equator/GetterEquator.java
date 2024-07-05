package cn.hein.common.equator;

import cn.hutool.core.util.StrUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Compares two objects based on their getter methods.
 *
 * @author hein
 */
public class GetterEquator extends AbstractEquator {

    private static final String GET_PREFIX = "get";
    private static final String IS_PREFIX = "is";
    private static final String GET_IS_PATTERN = "get|is";
    private static final String GET_CLASS_METHOD_NAME = "getClass";
    private static final Map<Class<?>, Map<String, Method>> METHOD_CACHE = new ConcurrentHashMap<>();

    public GetterEquator() {}

    public GetterEquator(boolean bothExistFieldOnly) {
        super(bothExistFieldOnly);
    }

    public GetterEquator(List<String> includeFields, List<String> excludeFields) {
        super(includeFields, excludeFields);
    }

    public GetterEquator(List<String> includeFields, List<String> excludeFields, boolean bothExistFieldOnly) {
        super(includeFields, excludeFields, bothExistFieldOnly);
    }

    /**
     * Identifies and returns a list of differing fields between two objects.
     *
     * @param o1 First object to compare.
     * @param o2 Second object to compare.
     * @return List of differing fields.
     */
    @Override
    public List<FieldInfo> getDifferentFields(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return Collections.emptyList();
        }
        // Attempt to determine if objects are simple data types
        if (isSimpleField(o1, o2)) {
            return compareSimpleField(o1, o2);
        }
        // Retrieve all getter methods for each object
        Map<String, Method> gettersOfO1 = getAllGetters(o1);
        Map<String, Method> gettersOfO2 = getAllGetters(o2);
        List<FieldInfo> differingFields = new LinkedList<>();
        for (String fieldName : getAllFieldNames(gettersOfO1.keySet(), gettersOfO2.keySet())) {
            try {
                Method getterOfO1 = gettersOfO1.getOrDefault(fieldName, null);
                Method getterOfO2 = gettersOfO2.getOrDefault(fieldName, null);
                FieldInfo fieldInfo = new FieldInfo(fieldName,
                        getReturnType(getterOfO1),
                        getReturnType(getterOfO2),
                        getterOfO1 != null ? getterOfO1.invoke(o1) : null,
                        getterOfO2 != null ? getterOfO2.invoke(o2) : null);
                if (!isFieldEquals(fieldInfo)) {
                    differingFields.add(fieldInfo);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("Exception occurred while accessing property for comparison: " + fieldName, e);
            }
        }
        return differingFields;
    }

    /**
     * Retrieves the return type of a method.
     *
     * @param method The method to inspect.
     * @return The return type of the method, or null if the method is null.
     */
    private Class<?> getReturnType(Method method) {
        return method == null ? null : method.getReturnType();
    }

    /**
     * Retrieves all getter methods of an object, caching results for performance.
     *
     * @param obj The object to inspect.
     * @return A map of field names to getter methods.
     */
    private Map<String, Method> getAllGetters(Object obj) {
        if (obj == null) {
            return Collections.emptyMap();
        }
        return METHOD_CACHE.computeIfAbsent(obj.getClass(), k -> {
            Class<?> clazz = k;
            Map<String, Method> getters = new LinkedHashMap<>(8);
            while (clazz != Object.class) {
                Method[] methods = clazz.getDeclaredMethods();
                for (Method m : methods) {
                    if (!Modifier.isPublic(m.getModifiers()) || m.getParameterTypes().length > 0) {
                        continue;
                    }
                    if (m.getReturnType() == Boolean.class || m.getReturnType() == boolean.class) {
                        if (m.getName().startsWith(IS_PREFIX)) {
                            String fieldName = StrUtil.lowerFirst(m.getName().substring(2));
                            getters.put(fieldName, m);
                            continue;
                        }
                    }
                    if (m.getName().startsWith(GET_PREFIX) && !GET_CLASS_METHOD_NAME.equals(m.getName())) {
                        String fieldName = StrUtil.lowerFirst(m.getName().replaceFirst(GET_IS_PATTERN, ""));
                        getters.put(fieldName, m);
                    }
                }
                clazz = clazz.getSuperclass();
            }
            return getters;
        });
    }
}