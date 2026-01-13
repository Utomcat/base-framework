package com.ranyk.entrance.config.properties;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: FileConfigProperties.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 文件上传路径配置
 * @date: 2026-01-09
 */
@Data
@Component
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadConfigProperties implements InitializingBean {
    /**
     * 文件上传根路径
     */
    private String path;

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
        // 如果未配置文件上传的根路径,则设置为默认的路径
        if (StrUtil.isBlank(path)){
            this.path = "E:/FTP/upload";
        }
    }
}
