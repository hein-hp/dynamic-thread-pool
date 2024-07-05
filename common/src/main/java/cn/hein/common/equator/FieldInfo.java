package cn.hein.common.equator;

import lombok.Data;

/**
 * Represents information about a field that differs between two objects.
 *
 * @author Dadiyang
 */
@Data
public class FieldInfo {

    /**
     * Name of the field that differs.
     */
    private String fieldName;

    /**
     * Type of the field in the first object.
     */
    private Class<?> type1;

    /**
     * Type of the field in the second object.
     */
    private Class<?> type2;

    /**
     * Value of the field in the first object.
     */
    private Object value1;

    /**
     * Value of the field in the second object.
     */
    private Object value2;

    public FieldInfo() {}

    public FieldInfo(String fieldName, Class<?> type1, Class<?> type2) {
        this(fieldName, type1, type2, null, null);
    }

    public FieldInfo(String fieldName, Class<?> fieldType, Object value1, Object value2) {
        this(fieldName, fieldType, fieldType, value1, value2);
    }

    public FieldInfo(String fieldName, Class<?> type1, Class<?> type2, Object value1, Object value2) {
        this.fieldName = fieldName;
        this.type1 = type1;
        this.type2 = type2;
        this.value1 = value1;
        this.value2 = value2;
    }
}