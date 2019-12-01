package com.leyou.lmhitysu.order.config;

import com.leyou.lmhitysu.common.utils.IdWorker;
import com.leyou.lmhitysu.order.properties.IdWorkerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(IdWorkerProperties.class)
public class IdWorkerConfig {
    @Bean
    public IdWorker idWorker(IdWorkerProperties properties){
        return new IdWorker(properties.getWorkerId(),properties.getDataCenterId());
    }
}
