package cn.hein.core.toolkit;

import cn.hutool.core.util.StrUtil;

/**
 * 字符串工具方法
 *
 * @author hein
 */
public class StringUtil {

    /**
     * 将遵循短横线（kebab-case）命名规则的字符串转换为驼峰命名（camelCase）格式。
     *
     * @param snakeCaseStr 待转换的短横线分隔字符串（kebab-case格式），
     *                     例如："example-long-string".
     * @return 转换后的驼峰命名格式字符串（camelCase格式），
     * 例如："exampleLongString". 如果输入为空字符串，则返回空字符串。
     */
    public static String snakeCaseToCamelCase(String snakeCaseStr) {
        if (StrUtil.isBlank(snakeCaseStr)) {
            return "";
        }
        StringBuilder camelCaseStr = new StringBuilder();
        boolean nextUpperCase = false;
        for (char c : snakeCaseStr.toCharArray()) {
            if (c == '-') {
                nextUpperCase = true;
            } else {
                if (nextUpperCase) {
                    camelCaseStr.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    camelCaseStr.append(c);
                }
            }
        }
        return camelCaseStr.toString();
    }
}
