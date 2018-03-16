package com.xyny.config;

import lombok.Data;

/**
 * @author xiaosuda
 * @date 2017/12/26
 */
@Data
public class DbConfigModal {
    private String  url;
    private String  username;
    private String  password;
    private String  driverClassName;
    private Integer initialSize;
    private Integer minIdle;
    private Integer maxActive;
    private Integer timeBetweenConnectErrorMillis;
    private Integer minEvictableIdleTimeMillis;
    private boolean testWhileIdle;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private String  validationQuery;
}
