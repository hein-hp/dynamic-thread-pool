package cn.hein.common.enums.executors;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.*;

/**
 * Enumeration class for thread pool rejection policies.
 *
 * @author hein
 */
@Getter
@AllArgsConstructor
public enum RejectionPolicyTypeEnum {

    /**
     * Abort policy
     */
    ABORT("Abort", new ThreadPoolExecutor.AbortPolicy()),

    /**
     * Discard policy
     */
    DISCARD("Discard", new ThreadPoolExecutor.DiscardPolicy()),

    /**
     * Discard the oldest policy
     */
    DISCARD_OLDEST("DiscardOldest", new ThreadPoolExecutor.DiscardOldestPolicy()),

    /**
     * Caller runs policy
     */
    CALLER_RUNS("CallerRuns", new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * The name of the rejection policy.
     */
    private final String name;

    /**
     * The instance of the rejection handler.
     */
    private final RejectedExecutionHandler handler;

    /**
     * Retrieves a rejection policy instance based on the provided name.
     *
     * @param name The name identifying the rejection policy.
     * @return An instance of the specified rejection policy.
     * @throws IllegalArgumentException If the policy type is unknown.
     */
    public static RejectedExecutionHandler getRejectionPolicy(String name) {
        for (RejectionPolicyTypeEnum type : RejectionPolicyTypeEnum.values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type.handler;
            }
        }
        throw new IllegalArgumentException("Unknown rejection policy type: " + name);
    }
}