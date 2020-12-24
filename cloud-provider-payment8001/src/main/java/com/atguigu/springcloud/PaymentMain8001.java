package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Auther: hzp
 * @Date: 2020/12/8 15:40
 * @Description:
 */
@SpringBootApplication
@EnableEurekaClient //标识为服务提供者
@EnableDiscoveryClient //暴露微服务信息
public class PaymentMain8001 {

    public static void main(String[] args){
        SpringApplication.run(PaymentMain8001.class, args);
    }
}
