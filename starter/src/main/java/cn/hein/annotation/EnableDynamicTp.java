package cn.hein.annotation;

import cn.hein.config.DynamicTpConfigurationSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enabled Dynamic Tp
 *
 * @author hein
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(DynamicTpConfigurationSelector.class)
public @interface EnableDynamicTp {
}
