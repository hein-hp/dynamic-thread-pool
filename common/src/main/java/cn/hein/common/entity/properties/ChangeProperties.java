package cn.hein.common.entity.properties;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Configuration properties class for notify when change
 *
 * @author hein
 */
@Data
public class ChangeProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 2407904778221150847L;

    /**
     * Whether to notify configuration changes
     */
    private boolean enabled = true;

    /**
     * Notification platform key.
     */
    private String platformKey;
}
