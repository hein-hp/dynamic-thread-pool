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
     * 将给定的 yaml 字符串内容转换为指定类类型的对象实例。
     *
     * @param <T>     目标对象的类型参数。
     * @param content yaml 格式的字符串内容，需要被转换。
     * @param clazz   目标类的 Class 对象，用于指定转换的目标类型。
     * @return 转换后的对象实例，其类型与提供的 clazz 参数匹配。
     */
    public static <T> T convert(String content, Class<T> clazz) {
        Map<String, Object> converted = new HashMap<>();
        convertKeysToCamelCase(instance.load(content), converted);
        return instance.loadAs(instance.dump(converted), clazz);
    }

    /**
     * 将给定的 yaml 字符串内容转换为指定类类型的对象实例，并在转换前从所有键名中移除指定的前缀。
     *
     * @param <T>     目标对象的类型参数。
     * @param content yaml 格式的字符串内容，待转换。
     * @param clazz   目标类的 class 对象，指明要转换成的类型。
     * @param prefix  一个或多个驼峰命名的字符串，表示要从键名中移除的前缀。
     *                如果有多个前缀，所有指定的前缀都将尝试从键名中移除。
     * @return 转换并处理后的对象实例，符合 clazz 指定的类型。
     */
    public static <T> T convertWithoutPrefix(String content, Class<T> clazz, String... prefix) {
        Map<String, Object> convertedMap = new HashMap<>();
        convertKeysToCamelCase(instance.load(content), convertedMap);
        System.out.println("convertedMap = " + convertedMap);
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
