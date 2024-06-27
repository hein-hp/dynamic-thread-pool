package cn.hein.common.toolkit;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for converting string representations to {@link TimeUnit} instances.
 *
 * @author Hein
 */
public class TimeUnitConvertUtil {

    // Immutable map for efficient lookups, mapping lowercase unit names to TimeUnit enums.
    private static final Map<String, TimeUnit> timeUnitMap = Map.of(
            "seconds", TimeUnit.SECONDS,
            "minutes", TimeUnit.MINUTES,
            "hours", TimeUnit.HOURS,
            "days", TimeUnit.DAYS,
            "milliseconds", TimeUnit.MILLISECONDS,
            "microseconds", TimeUnit.MICROSECONDS,
            "nanoseconds", TimeUnit.NANOSECONDS
    );

    /**
     * Converts a string representation of a time unit to the corresponding {@link TimeUnit}.
     *
     * @param unitName The name of the time unit in lowercase (e.g., "seconds").
     * @return The matching {@link TimeUnit} instance.
     * @throws IllegalArgumentException If the provided unit name does not correspond to a known time unit.
     */
    public static TimeUnit convert(String unitName) {
        TimeUnit timeUnit = timeUnitMap.get(unitName);
        if (timeUnit == null) {
            throw new IllegalArgumentException("Unknown time unit: " + unitName);
        }
        return timeUnit;
    }
}

