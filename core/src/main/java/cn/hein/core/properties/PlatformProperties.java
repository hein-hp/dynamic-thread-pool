package cn.hein.core.properties;

import lombok.Data;

/**
 * 告警平台配置类
 *
 * @author hein
 */
@Data
public class PlatformProperties {

    /**
     * 告警平台（目前仅支持 email）
     */
    private String platform;

    /**
     * 接受人邮箱
     */
    private String reviveEmail;
}
