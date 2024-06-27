package cn.hein.core.properties;

import lombok.Data;

/**
 * Configuration properties class for logging output.
 *
 * @author hein
 */
@Data
public class LogProperties {

    /**
     * Whether to enable log output (default outputs to MySQL).
     */
    private boolean enable;
}
