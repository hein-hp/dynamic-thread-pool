package cn.hein.core.properties;

import lombok.Data;

/**
 * 监控信息配置类
 *
 * @author hein
 */
@Data
public class NotifyItemProperties {

    /**
     * 监控类型
     */
    private String type;

    /**
     * 是否开启
     */
    private boolean enabled;

    /**
     * 阈值
     */
    private double threshold;
}
