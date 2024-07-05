package cn.hein.core.refresher;

import cn.hein.common.constant.executor.NacosConstant;
import cn.hein.common.entity.properties.DynamicTpProperties;
import cn.hein.common.enums.properties.PropertiesTypeEnum;
import cn.hein.common.toolkit.StringUtil;
import cn.hein.core.context.DynamicTpContext;
import cn.hein.core.context.NotifyPlatformContext;
import cn.hein.core.notifier.NotifyManager;
import cn.hein.core.support.PropertiesBindHelper;
import cn.hein.core.support.PropertiesParseHelper;
import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Abstract base class for implementing refreshers that update dynamic thread pool configurations.
 *
 * @author hein
 */
@Slf4j
public abstract class AbstractRefresher implements Refresher {

    private final DynamicTpProperties properties;
    private final DynamicTpContext tpContext;
    private final NotifyPlatformContext pfContext;

    protected AbstractRefresher(DynamicTpProperties properties,
                                DynamicTpContext tpContext,
                                NotifyPlatformContext pfContext) {
        this.properties = properties;
        this.tpContext = tpContext;
        this.pfContext = pfContext;
    }

    /**
     * Refreshes the dynamic thread pool properties from a property map.
     *
     * @param content The properties content as a string.
     * @param type    The type of properties.
     */
    @Override
    public void refresh(String content, PropertiesTypeEnum type) {
        refresh(PropertiesParseHelper.getInstance().parseProperties(content, type));
    }

    /**
     * Refreshes the dynamic thread pool properties from a property map.
     *
     * @param prop The properties map to bind.
     */
    protected void refresh(Map<Object, Object> prop) {
        DynamicTpProperties oldProp = beforeRefresh();
        PropertiesBindHelper.bindProperties(prop, properties);
        // set beanName
        properties.getExecutors()
                .forEach(each -> each.setBeanName(StringUtil.kebabCaseToCamelCase(each.getThreadPoolName())));
        doRefresh(oldProp);
        afterRefresh();
    }

    /**
     * Refreshes the dynamic thread pool properties from the Spring Environment.
     *
     * @param environment The Spring Environment containing the properties.
     */
    protected void refresh(Environment environment) {
        DynamicTpProperties oldProp = beforeRefresh();
        PropertiesBindHelper.bindProperties(environment, properties);
        // set beanName
        properties.getExecutors()
                .forEach(each -> each.setBeanName(StringUtil.kebabCaseToCamelCase(each.getThreadPoolName())));
        doRefresh(oldProp);
        afterRefresh();
    }

    /**
     * Performs the actual refresh operation by updating the context with new properties.
     *
     * @param oldProp The previous properties before the refresh.
     */
    private void doRefresh(DynamicTpProperties oldProp) {
        tpContext.refresh(oldProp);
        pfContext.refresh();
        NotifyManager.refresh();
    }

    /**
     * Determines whether a refresh is needed based on the keys that have been changed.
     *
     * @param changedKeys The set of keys that have changed.
     * @return true if a refresh is needed, false otherwise.
     */
    protected boolean needRefresh(Set<String> changedKeys) {
        if (CollUtil.isEmpty(changedKeys)) {
            return false;
        }
        changedKeys = changedKeys.stream()
                .filter(key -> key.startsWith(NacosConstant.MAIN_PREFIX))
                .collect(Collectors.toSet());
        return CollUtil.isNotEmpty(changedKeys);
    }

    /**
     * Method called after the refresh operation has been performed.
     */
    protected abstract void afterRefresh();

    /**
     * Method called before the refresh operation to prepare the old properties.
     *
     * @return The old properties before the refresh.
     */
    protected abstract DynamicTpProperties beforeRefresh();
}