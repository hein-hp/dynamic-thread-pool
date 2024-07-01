package cn.hein.core.refresher;

import cn.hein.common.enums.properties.PropertiesTypeEnum;

/**
 * Defines the contract for a refresher component capable of updating properties within the system.
 *
 * @author hein
 */
public interface Refresher {

    /**
     * Refreshes properties with the specified content and property type.
     *
     * @param content The content string containing the new property values.
     * @param type    The type of properties being updated, determining how the content should be parsed.
     */
    void refresh(String content, PropertiesTypeEnum type);
}