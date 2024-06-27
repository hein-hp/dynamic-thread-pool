package cn.hein.core.properties;

import lombok.Data;

/**
 * 日志输出配置类
 *
 * @author hein
 */
@Data
public class LogProperties {

    /**
     * 是否开启日志输出（默认输出到 MySQL）
     */
    private boolean enable;
}
