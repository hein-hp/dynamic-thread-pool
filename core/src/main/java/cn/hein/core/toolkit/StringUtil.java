package cn.hein.core.toolkit;

/**
 * 字符串工具方法
 *
 * @author hein
 */
public class StringUtil {

    /**
     * 将短横线分隔的字符串转换为驼峰命名。
     *
     * @param snakeCaseStr 短横线分隔的字符串，如 "this-is-a-test"
     * @return 驼峰命名的字符串，如 "thisIsATest"
     */
    public static String snakeCaseToCamelCase(String snakeCaseStr) {
        if (cn.hutool.core.util.StrUtil.isBlank(snakeCaseStr)) {
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
