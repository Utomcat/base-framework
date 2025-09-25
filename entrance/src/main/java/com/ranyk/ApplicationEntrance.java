package com.ranyk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * CLASS_NAME: ApplicationEntrance.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 应用入口程序
 * @date: 2025-09-25
 */
@SpringBootApplication
@EnableConfigurationProperties
public class ApplicationEntrance {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationEntrance.class, args);
    }
}
