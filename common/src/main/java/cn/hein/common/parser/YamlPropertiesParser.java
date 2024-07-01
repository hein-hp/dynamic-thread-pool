package cn.hein.common.parser;

import cn.hein.common.enums.properties.PropertiesTypeEnum;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ByteArrayResource;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Parser for YAML properties.
 *
 * @author hein
 */
public class YamlPropertiesParser extends AbstractPropertiesParser {

    private static final List<PropertiesTypeEnum> SUPPORTED_TYPES = List.of(PropertiesTypeEnum.YAML);

    @Override
    public List<PropertiesTypeEnum> types() {
        return SUPPORTED_TYPES;
    }

    @Override
    public Map<Object, Object> doParse(String content) {
        if (StrUtil.isBlank(content)) {
            return Collections.emptyMap();
        }
        YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();
        factoryBean.setResources(new ByteArrayResource(content.getBytes()));
        factoryBean.afterPropertiesSet(); // Ensure the factory bean is fully initialized
        return factoryBean.getObject();
    }
}