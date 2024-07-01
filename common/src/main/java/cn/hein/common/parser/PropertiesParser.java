package cn.hein.common.parser;

import cn.hein.common.enums.properties.PropertiesTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * Interface defining a contract for property parsers.
 *
 * @author hein
 */
public interface PropertiesParser {

    /**
     * Determines if the parser supports the given property type.
     *
     * @param type The property type to check.
     * @return true if the parser supports the type, false otherwise.
     */
    boolean supports(PropertiesTypeEnum type);

    /**
     * Returns a list of property types supported by the parser.
     *
     * @return A List of PropertiesTypeEnum values.
     */
    List<PropertiesTypeEnum> types();

    /**
     * Parses the given content into a map of properties.
     *
     * @param content The string content to parse.
     * @return A Map containing the parsed properties.
     */
    Map<Object, Object> doParse(String content);
}