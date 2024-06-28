package cn.hein.common.toolkit;

/**
 * Utility class providing methods for class-related operations.
 *
 * @author hein
 */
public final class ClassUtil {

    /**
     * The character used as a separator in package names.
     */
    private static final char PACKAGE_SEPARATOR_CHAR = '.';

    /**
     * Returns the simple name of the given class.
     *
     * @param clazz the class to get the simple name from
     * @return the simple name of the class
     * @throws NullPointerException if the provided class is null
     */
    public static String simpleClassName(Class<?> clazz) {
        if (clazz == null) {
            throw new NullPointerException("The class cannot be null.");
        }
        String className = clazz.getName();
        final int lastDotIdx = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        if (lastDotIdx > -1) {
            return className.substring(lastDotIdx + 1);
        }
        return className;
    }
}