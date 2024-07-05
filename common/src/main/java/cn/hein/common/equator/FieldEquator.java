package cn.hein.common.equator;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An equator that compares objects based on their fields.
 *
 * @author hein
 */
public class FieldEquator extends AbstractEquator {

    private static final Map<Class<?>, Map<String, Field>> FIELD_CACHE = new ConcurrentHashMap<>();

    public FieldEquator() {}

    public FieldEquator(boolean bothExistFieldOnly) {
        super(bothExistFieldOnly);
    }

    public FieldEquator(List<String> includeFields, List<String> excludeFields) {
        super(includeFields, excludeFields);
    }

    public FieldEquator(List<String> includeFields, List<String> excludeFields, boolean bothExistFieldOnly) {
        super(includeFields, excludeFields, bothExistFieldOnly);
    }

    /**
     * Compares two objects and returns a list of differing fields.
     *
     * @param o1 First object to compare.
     * @param o2 Second object to compare.
     * @return List of fields that differ between the two objects.
     */
    @Override
    public List<FieldInfo> getDifferentFields(Object o1, Object o2) {
        if (o1 == o2) {
            return Collections.emptyList();
        }
        // Attempt to determine if objects are simple data types
        if (isSimpleField(o1, o2)) {
            return compareSimpleField(o1, o2);
        }
        // Retrieve all fields for each object
        Map<String, Field> fieldsOfO1 = getAllFields(o1);
        Map<String, Field> fieldsOfO2 = getAllFields(o2);
        List<FieldInfo> differingFields = new ArrayList<>();
        for (String fieldName : getAllFieldNames(fieldsOfO1.keySet(), fieldsOfO2.keySet())) {
            try {
                Field fieldOfO1 = fieldsOfO1.getOrDefault(fieldName, null);
                Field fieldOfO2 = fieldsOfO2.getOrDefault(fieldName, null);
                Object valueOfO1 = null, valueOfO2 = null;
                Class<?> typeOfO1 = null, typeOfO2 = null;
                if (fieldOfO1 != null) {
                    fieldOfO1.setAccessible(true);
                    valueOfO1 = fieldOfO1.get(o1);
                    typeOfO1 = fieldOfO1.getType();
                }
                if (fieldOfO2 != null) {
                    fieldOfO2.setAccessible(true);
                    valueOfO2 = fieldOfO2.get(o2);
                    typeOfO2 = fieldOfO2.getType();
                }
                FieldInfo fieldInfo = new FieldInfo(fieldName, typeOfO1, typeOfO2, valueOfO1, valueOfO2);
                if (!isFieldEquals(fieldInfo)) {
                    differingFields.add(fieldInfo);
                }
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Exception occurred while accessing field for comparison: " + fieldName, e);
            }
        }
        return differingFields;
    }

    /**
     * Retrieves all fields of an object, caching results for performance.
     *
     * @param obj The object to retrieve fields from.
     * @return A map of field names to fields.
     */
    private Map<String, Field> getAllFields(Object obj) {
        if (obj == null) {
            return Collections.emptyMap();
        }
        return FIELD_CACHE.computeIfAbsent(obj.getClass(), k -> {
            Map<String, Field> fieldMap = new HashMap<>(8);
            Class<?> clazz = k;
            while (clazz != Object.class) {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    // Exclude synthetic fields typically added by bytecode manipulation frameworks
                    if (!field.isSynthetic()) {
                        fieldMap.put(field.getName(), field);
                    }
                }
                clazz = clazz.getSuperclass();
            }
            return fieldMap;
        });
    }
}