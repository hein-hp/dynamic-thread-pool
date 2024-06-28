package cn.hein.common.toolkit;

import org.yaml.snakeyaml.Yaml;

import java.util.Map;

/**
 * Utility class for manipulating YAML data.
 *
 * @author Hein
 */
public class YamlUtil {

    private static final Yaml instance = new Yaml();

    /**
     * Converts a YAML string into an object of the specified type.
     *
     * @param content The YAML string to convert.
     * @param clazz   The class type to which the YAML should be converted.
     * @param <T>     The generic type of the class.
     * @return An instance of the specified class populated with data from the YAML string.
     */
    public static <T> T convert(String content, Class<T> clazz) {
        return instance.loadAs(content, clazz);
    }

    /**
     * Converts a YAML string into an object of the specified type, removing any prefixed keys.
     *
     * @param content The YAML string to convert.
     * @param clazz   The class type to which the YAML should be converted.
     * @param prefix  The prefixed keys to remove before conversion.
     * @param <T>     The generic type of the class.
     * @return An instance of the specified class populated with data from the YAML string.
     */
    public static <T> T convertWithoutPrefix(String content, Class<T> clazz, String... prefix) {
        return instance.loadAs(removePrefix(content, prefix), clazz);
    }

    /**
     * Removes prefixed keys from a YAML string.
     *
     * @param content The YAML string to process.
     * @param prefix  The prefixed keys to remove.
     * @return A String containing the YAML data without the prefixed keys.
     */
    @SuppressWarnings("unchecked")
    private static String removePrefix(String content, String... prefix) {
        Map<String, Object> map = new Yaml().load(content);
        for (String level : prefix) {
            if (map != null && map.containsKey(level)) {
                map = (Map<String, Object>) map.get(level);
            } else {
                throw new IllegalArgumentException("Prefix '" + level + "' not found in YAML content.");
            }
        }
        if (map == null) {
            throw new IllegalStateException("Unable to remove prefixes; final map is null.");
        }
        return new Yaml().dump(map);
    }
}

