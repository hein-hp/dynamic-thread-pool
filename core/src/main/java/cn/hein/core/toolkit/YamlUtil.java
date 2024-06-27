package cn.hein.core.toolkit;

import org.yaml.snakeyaml.Yaml;

import java.util.*;

import static cn.hein.core.toolkit.StringUtil.snakeCaseToCamelCase;

/**
 * Yaml 工具类
 *
 * @author hein
 */
public class YamlUtil {

    private static final Yaml instance = new Yaml();

    /**
     * Converts the given YAML string content into an object instance of the specified class type.
     *
     * @param <T>     The type parameter of the target object.
     * @param content The YAML formatted string content to be converted.
     * @param clazz   The Class object of the target, specifying the type to convert to.
     * @return An object instance converted, matching the type specified by clazz.
     */
    public static <T> T convert(String content, Class<T> clazz) {
        Map<String, Object> converted = new HashMap<>();
        convertKeysToCamelCase(instance.load(content), converted);
        return instance.loadAs(instance.dump(converted), clazz);
    }

    /**
     * Converts the given YAML string content into an object instance of the specified class type,
     * removing specified prefixes from all keys before conversion.
     *
     * @param <T>     The type parameter of the target object.
     * @param content The YAML formatted string content to be converted.
     * @param clazz   The Class object of the target, indicating the type to convert to.
     * @param prefix  One or more camelCase strings representing prefixes to remove from keys.
     *                If multiple prefixes are provided, all will be attempted to be removed from keys.
     * @return A converted and processed object instance, conforming to the type specified by clazz.
     */
    public static <T> T convertWithoutPrefix(String content, Class<T> clazz, String... prefix) {
        Map<String, Object> convertedMap = new HashMap<>();
        convertKeysToCamelCase(instance.load(content), convertedMap);
        return instance.loadAs(removePrefix(instance.dump(convertedMap), prefix), clazz);
    }

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

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void convertKeysToCamelCase(Map<String, Object> origin, Map<String, Object> converted) {
        if (origin == null || converted == null) {
            throw new IllegalArgumentException("Both 'origin' and 'converted' Maps must not be null.");
        }
        for (String key : origin.keySet()) {
            Object value = origin.get(key);
            String camelCaseKey = snakeCaseToCamelCase(key);
            converted.put(camelCaseKey, value);
            if (!key.equals(camelCaseKey)) {
                converted.remove(key);
            }
            if (value instanceof Map nestedMap) {
                convertKeysToCamelCase(nestedMap, (Map<String, Object>) converted.computeIfAbsent(camelCaseKey, k -> new LinkedHashMap<>()));
            } else if (value instanceof List<?> list) {
                List<Object> camelCaseList = new ArrayList<>(list.size());
                list.forEach(each -> {
                    if (each instanceof Map originalMap) {
                        Map<String, Object> camelCaseEachMap = new LinkedHashMap<>();
                        convertKeysToCamelCase(originalMap, camelCaseEachMap);
                        camelCaseList.add(camelCaseEachMap);
                    } else {
                        camelCaseList.add(each);
                    }
                });
                converted.put(camelCaseKey, camelCaseList);
            }
        }
    }
}
