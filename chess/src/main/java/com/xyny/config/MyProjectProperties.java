package com.xyny.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author xiaosuda
 * @date 2017/12/26
 **/
@Configuration
@Data
public class MyProjectProperties {

    @ConfigurationProperties(prefix = "druid.datasource")
    @Bean
    public DbConfigModal getDbConfigModal() {
        return new DbConfigModal();
    }
}
