package cn.hein.core.config;

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

    /**
     * 数据库驱动
     */
    private String driver;

    /**
     * 数据库连接 Url
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
