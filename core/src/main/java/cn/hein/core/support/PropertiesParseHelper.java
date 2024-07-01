package cn.hein.core.support;

import cn.hein.common.enums.properties.PropertiesTypeEnum;
import cn.hein.common.parser.PropertiesParser;
import cn.hein.common.parser.YamlPropertiesParser;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Helper class for parsing properties based on the specified type.
 *
 * @author hein
 */
public final class PropertiesParseHelper {

    private static final List<PropertiesParser> PARSERS = List.of(
            new YamlPropertiesParser()
    );

    private PropertiesParseHelper() {}

    /**
     * Parses the given content into a map of properties based on the specified type.
     *
     * @param content The content to be parsed.
     * @param type    The type of properties to parse.
     * @return A map of parsed properties, or an empty map if no suitable parser is found.
     */
    public Map<Object, Object> parseProperties(String content, PropertiesTypeEnum type) {
        return PARSERS.stream()
                .filter(parser -> parser.supports(type))
                .findFirst()
                .map(parser -> parser.doParse(content))
                .orElse(Collections.emptyMap());
    }

    /**
     * Returns the singleton instance of PropertiesParseHelper.
     *
     * @return The singleton instance.
     */
    public static PropertiesParseHelper getInstance() {
        return PropertiesHandlerHolder.INSTANCE;
    }

    private static class PropertiesHandlerHolder {
        private static final PropertiesParseHelper INSTANCE = new PropertiesParseHelper();
    }
}