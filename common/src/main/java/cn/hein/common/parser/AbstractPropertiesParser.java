package cn.hein.common.parser;

import cn.hein.common.enums.properties.PropertiesTypeEnum;

/**
 * Abstract base class for property parsers.
 *
 * @author hein
 */
public abstract class AbstractPropertiesParser implements PropertiesParser {

    @Override
    public boolean supports(PropertiesTypeEnum type) {
        return this.types().contains(type);
    }
}