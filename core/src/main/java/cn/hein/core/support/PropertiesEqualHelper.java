package cn.hein.core.support;

import cn.hein.common.equator.Equator;
import cn.hein.common.equator.FieldEquator;
import cn.hein.common.equator.FieldInfo;

import java.util.List;

/**
 * Provides utilities for comparing the equality of properties between two objects,
 * and retrieving information about differing fields.
 * This class should not be instantiated.
 * <p>
 * Example usage:
 * <pre>
 * boolean isEqual = PropertiesEqualHelper.equal(object1, object2);
 * List<FieldInfo> differentFields = PropertiesEqualHelper.getDifferentFields(object1, object2);
 * </pre>
 *
 * @author hein
 */
public final class PropertiesEqualHelper {

    private static final Equator equator = new FieldEquator(true);

    private PropertiesEqualHelper() {}

    /**
     * Determines whether two objects are equal based on their properties.
     *
     * @param o1 First object to compare.
     * @param o2 Second object to compare.
     * @return True if the objects are equal based on their properties, false otherwise.
     */
    public static boolean equal(Object o1, Object o2) {
        return equator.equal(o1, o2);
    }

    /**
     * Returns a list of FieldInfo instances representing fields that differ between two objects.
     *
     * @param o1 First object to compare.
     * @param o2 Second object to compare.
     * @return A List containing FieldInfo instances for fields where the values differ.
     */
    public static List<FieldInfo> getDifferentFields(Object o1, Object o2) {
        return equator.getDifferentFields(o1, o2);
    }
}