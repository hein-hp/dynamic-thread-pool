package cn.hein.common.equator;

import java.util.List;

/**
 * Interface defining methods for deep comparison of two objects.
 *
 * @author hein
 */
public interface Equator {

    /**
     * Compares two objects for deep equality.
     *
     * @param o1 The first object to compare.
     * @param o2 The second object to compare.
     * @return true if both objects are deeply equal, false otherwise.
     */
    boolean equal(Object o1, Object o2);

    /**
     * Returns a list of fields that differ between the two provided objects.
     *
     * @param o1 The first object.
     * @param o2 The second object.
     * @return A list of FieldInfo objects representing the differing fields.
     */
    List<FieldInfo> getDifferentFields(Object o1, Object o2);
}