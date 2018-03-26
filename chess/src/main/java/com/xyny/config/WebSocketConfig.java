package com.xyny.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 *
 * @author xiaosuda
 * @date 2018/3/20
 */
@Configuration
public class WebSocketConfig {

    @Bean
    @Primary
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }


}
