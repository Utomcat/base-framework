package com.ranyk.entrance.config.properties;

import lombok.Data;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * CLASS_NAME: I18nConfigProperties.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 语言环境配置属性类
 * @date: 2025-12-18
 */
@Data
@Component
@ConfigurationProperties(prefix = "lang")
public class I18nConfigProperties implements InitializingBean {

    private String localLang;

    private Locale locale;

    /**
     * Invoked by the containing {@code BeanFactory} after it has set all bean properties
     * and satisfied {@link BeanFactoryAware}, {@code ApplicationContextAware} etc.
     * <p>This method allows the bean instance to perform validation of its overall
     * configuration and final initialization when all bean properties have been set.
     *
     * @throws Exception in the event of misconfiguration (such as failure to set an
     *                   essential property) or if initialization fails for any other reason
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        switch (localLang) {
            case "en_US":
            case "US":
                locale = Locale.US;
                break;
            case "JAPAN":
                locale = Locale.JAPAN;
                break;
            case "KOREA":
                locale = Locale.KOREA;
                break;
            default:
                locale = Locale.CHINA;
        }

    }
}
