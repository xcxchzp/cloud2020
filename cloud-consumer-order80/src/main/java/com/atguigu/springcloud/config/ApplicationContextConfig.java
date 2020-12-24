package com.atguigu.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Auther: hzp
 * @Date: 2020/12/9 12:03
 * @Description:
 */
@Configuration
public class ApplicationContextConfig {

    @Bean
//    @LoadBalanced
    //为RestTemplate 提供负载均衡功能
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
