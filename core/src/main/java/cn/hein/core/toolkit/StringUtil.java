package cn.hein.core.toolkit;

import cn.hutool.core.util.StrUtil;

/**
 * Utility class for string manipulation.
 *
 * @author Hein
 */
public class StringUtil {

    /**
     * Converts a snake-case formatted string to camelCase format.
     *
     * @param kebabCaseStr The string in kebab-case format (e.g., "example-long-string")
     *                     to be converted to camelCase. If an empty string is input,
     *                     an empty string is returned.
     * @return The converted string in camelCase format (e.g., "exampleLongString").
     */
    public static String snakeCaseToCamelCase(String kebabCaseStr) {
        if (StrUtil.isBlank(kebabCaseStr)) {
            return "";
        }
        StringBuilder camelCaseStr = new StringBuilder();
        boolean nextUpperCase = false;
        for (char c : kebabCaseStr.toCharArray()) {
            if (c == '-') {
                nextUpperCase = true;
            } else {
                camelCaseStr.append(nextUpperCase ? Character.toUpperCase(c) : c);
                nextUpperCase = false;
            }
        }
        return camelCaseStr.toString();
    }
}
