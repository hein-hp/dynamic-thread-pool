package cn.hein.core.common.enums.executors;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.*;

/**
 * 拒绝策略枚举类
 *
 * @author hein
 */
@Getter
@AllArgsConstructor
public enum RejectionPolicyTypeEnum {

    /**
     * ABORT
     */
    ABORT("ABORT", new ThreadPoolExecutor.AbortPolicy()),

    /**
     * DISCARD
     */
    DISCARD("DISCARD", new ThreadPoolExecutor.DiscardPolicy()),

    /**
     * DISCARD_OLDEST
     */
    DISCARD_OLDEST("DISCARD_OLDEST", new ThreadPoolExecutor.DiscardOldestPolicy()),

    /**
     * CALLER_RUNS
     */
    CALLER_RUNS("CALLER_RUNS", new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 拒绝策略名称
     */
    private final String name;

    /**
     * 拒绝策略类型
     */
    private final RejectedExecutionHandler handler;

    public static RejectedExecutionHandler getRejectionPolicy(String name) {
        for (RejectionPolicyTypeEnum type : RejectionPolicyTypeEnum.values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type.handler;
            }
        }
        throw new IllegalArgumentException("Unknown rejection policy type: " + name);
    }
}
