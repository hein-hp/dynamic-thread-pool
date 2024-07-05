package cn.hein.common.equator;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;

import java.util.*;

/**
 * Abstract class implementing the Equator interface for comparing object equality.
 * Provides methods to compare two objects based on specified rules, such as field inclusion and exclusion.
 *
 * @author Hein
 */
@Data
public abstract class AbstractEquator implements Equator {

    private static final List<Class<?>> PRIMITIVE_WRAPPERS = Arrays.asList(Byte.class, Short.class, Integer.class, Long.class,
            Float.class, Double.class, Character.class, Boolean.class, String.class);

    /**
     * Fields to include in comparison. If null or empty, no specific fields are included.
     */
    private List<String> includeFields;

    /**
     * Fields to exclude from comparison. If null or empty, no specific fields are excluded.
     */
    private List<String> excludeFields;

    /**
     * Flag indicating whether to compare only common fields of different classes.
     * Effective only when comparing objects of different types.
     */
    private boolean bothExistFieldOnly;

    public AbstractEquator() {
        this(true);
    }

    public AbstractEquator(boolean bothExistFieldOnly) {
        this(null, null, bothExistFieldOnly);
    }

    public AbstractEquator(List<String> includeFields, List<String> excludeFields) {
        this(includeFields, excludeFields, true);
    }

    public AbstractEquator(List<String> includeFields, List<String> excludeFields, boolean bothExistFieldOnly) {
        this.includeFields = includeFields == null ? Collections.emptyList() : includeFields;
        this.excludeFields = excludeFields == null ? Collections.emptyList() : excludeFields;
        this.bothExistFieldOnly = bothExistFieldOnly;
    }

    /**
     * Determines if two objects are equal by comparing their different fields.
     *
     * @param o1 First object to compare.
     * @param o2 Second object to compare.
     * @return True if objects are equal, false otherwise.
     */
    @Override
    public boolean equal(Object o1, Object o2) {
        List<FieldInfo> differences = getDifferentFields(o1, o2);
        return differences == null || differences.isEmpty();
    }

    /**
     * Compares two fields for equality. Can be overridden in subclasses for custom comparison logic.
     *
     * @param fieldInfo Information about the field being compared.
     * @return True if fields are considered equal, false otherwise.
     */
    protected boolean isFieldEquals(FieldInfo fieldInfo) {
        if (isExcluded(fieldInfo) || !isIncluded(fieldInfo)) {
            return true;
        }
        return nullableEquals(fieldInfo.getValue1(), fieldInfo.getValue2());
    }

    /**
     * Determines if a field should be included in the comparison.
     *
     * @param fieldInfo Information about the field.
     * @return True if the field should be included, false otherwise.
     */
    protected boolean isIncluded(FieldInfo fieldInfo) {
        if (includeFields == null || includeFields.isEmpty()) {
            return true;
        }
        return includeFields.contains(fieldInfo.getFieldName());
    }

    /**
     * Determines if a field should be excluded from the comparison.
     *
     * @param fieldInfo Information about the field.
     * @return True if the field should be excluded, false otherwise.
     */
    protected boolean isExcluded(FieldInfo fieldInfo) {
        if (excludeFields != null && !excludeFields.isEmpty()) {
            return excludeFields.contains(fieldInfo.getFieldName());
        }
        return false;
    }

    /**
     * Compares simple fields for equality.
     *
     * @param o1 First object to compare.
     * @param o2 Second object to compare.
     * @return List of differing fields, or an empty list if objects are equal.
     */
    List<FieldInfo> compareSimpleField(Object o1, Object o2) {
        if (Objects.equals(o1, o2)) {
            return Collections.emptyList();
        } else {
            Class<?> clazz = (o1 == null ? o2 : o1).getClass();
            return Collections.singletonList(new FieldInfo(clazz.getSimpleName(), clazz, o1, o2));
        }
    }

    /**
     * Determines if a field is a primitive or a wrapper type.
     *
     * @param o1 First object to check.
     * @param o2 Second object to check.
     * @return True if the field is a primitive or wrapper type, false otherwise.
     */
    boolean isSimpleField(Object o1, Object o2) {
        Class<?> clazz = (o1 == null ? o2 : o1).getClass();
        return clazz.isPrimitive() || PRIMITIVE_WRAPPERS.contains(clazz);
    }

    /**
     * Determines whether to take the intersection or union of fields for comparison.
     *
     * @param fields1 First set of field names.
     * @param fields2 Second set of field names.
     * @return Set containing either the intersection or union of field names.
     */
    Set<String> getAllFieldNames(Set<String> fields1, Set<String> fields2) {
        return bothExistFieldOnly ? (Set<String>) CollUtil.intersection(fields1, fields2)
                : (Set<String>) CollUtil.union(fields1, fields2);
    }

    /**
     * Performs a deep equality check on nullable objects, including collections.
     *
     * @param o1 First object to compare.
     * @param o2 Second object to compare.
     * @return True if objects are considered equal, false otherwise.
     */
    private boolean nullableEquals(Object o1, Object o2) {
        if (o1 instanceof Collection<?> c1 && o2 instanceof Collection<?> c2) {
            return Objects.deepEquals(c1.toArray(), c2.toArray());
        }
        return Objects.deepEquals(o1, o2);
    }
}