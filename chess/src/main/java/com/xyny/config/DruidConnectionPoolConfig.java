package com.xyny.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 *  druid 连接池 初始化
 * @author xiaosuda
 * @date 2017/12/26
 */
@Configuration
@ConfigurationProperties(prefix = "druid.datasource")
public class DruidConnectionPoolConfig {

    @Autowired
    private DbConfigModal dbConfigModal;

    @Bean
    @Primary
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(dbConfigModal.getUrl());
        dataSource.setUsername(dbConfigModal.getUsername());
        dataSource.setPassword(dbConfigModal.getPassword());
        dataSource.setDriverClassName(dbConfigModal.getDriverClassName());
        dataSource.setInitialSize(dbConfigModal.getInitialSize());
        dataSource.setMinIdle(dbConfigModal.getMinIdle());
        dataSource.setMaxActive(dbConfigModal.getMaxActive());
        dataSource.setTimeBetweenConnectErrorMillis(dbConfigModal.getTimeBetweenConnectErrorMillis());
        dataSource.setMinEvictableIdleTimeMillis(dbConfigModal.getMinEvictableIdleTimeMillis());
        dataSource.setTestWhileIdle(dbConfigModal.isTestWhileIdle());
        dataSource.setTestOnBorrow(dbConfigModal.isTestOnBorrow());
        dataSource.setTestOnReturn(dbConfigModal.isTestOnReturn());
        dataSource.setValidationQuery(dbConfigModal.getValidationQuery());
        return dataSource;
    }
}
