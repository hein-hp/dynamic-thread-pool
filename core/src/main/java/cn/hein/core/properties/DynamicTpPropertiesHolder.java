package cn.hein.core.properties;

import cn.hein.core.listener.DynamicTpInitListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.event.ApplicationReadyEvent;

/**
 * Holder class for {@link DynamicTpProperties}, intended for reference during subsequent Nacos configuration updates.
 * There is only one instance across the entire application context, initialized within the
 * {@link DynamicTpInitListener#onApplicationEvent(ApplicationReadyEvent)} method when the {@link ApplicationReadyEvent} is published.
 *
 * @author hein
 */
@Getter
@AllArgsConstructor
public class DynamicTpPropertiesHolder {

    private final DynamicTpProperties dynamicTpProperties;
}