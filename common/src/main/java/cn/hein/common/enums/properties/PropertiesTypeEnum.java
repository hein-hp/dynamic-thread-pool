package cn.hein.common.enums.properties;

/**
 * Enumeration class for Property type
 *
 * @author hein
 */
public enum PropertiesTypeEnum {

    /**
     * Text property type.
     */
    TEXT("text"),

    /**
     * JSON property type.
     */
    JSON("json"),

    /**
     * XML property type.
     */
    XML("xml"),

    /**
     * YAML property type.
     */
    YAML("yaml"),

    /**
     * HTML property type.
     */
    HTML("html"),

    /**
     * Properties file format type.
     */
    PROPERTIES("properties");

    private final String value;

    PropertiesTypeEnum(String value) {
        this.value = value;
    }

    /**
     * Converts a string representation of a property type to its corresponding enum value.
     * If the string does not match any known property type, YAML is returned as the default.
     *
     * @param value The string representation of the property type.
     * @return The corresponding PropertiesTypeEnum value.
     */
    public static PropertiesTypeEnum of(String value) {
        for (PropertiesTypeEnum typeEnum : PropertiesTypeEnum.values()) {
            if (typeEnum.value.equals(value)) {
                return typeEnum;
            }
        }
        return YAML; // default
    }
}