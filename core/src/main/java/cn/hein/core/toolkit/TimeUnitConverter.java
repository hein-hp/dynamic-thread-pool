package cn.hein.core.toolkit;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * TimeUnit 转换类
 *
 * @author hein
 */
public class TimeUnitConverter {

    private static final Map<String, TimeUnit> timeUnitMap = Map.of(
            "seconds", TimeUnit.SECONDS,
            "minutes", TimeUnit.MINUTES,
            "hours", TimeUnit.HOURS,
            "days", TimeUnit.DAYS,
            "milliseconds", TimeUnit.MILLISECONDS,
            "microseconds", TimeUnit.MICROSECONDS,
            "nanoseconds", TimeUnit.NANOSECONDS
    );

    public static TimeUnit convert(String name) {
        TimeUnit timeUnit = timeUnitMap.get(name.toLowerCase());
        if (timeUnit == null) {
            throw new IllegalArgumentException("Unknown time unit: " + name);
        }
        return timeUnit;
    }
}

