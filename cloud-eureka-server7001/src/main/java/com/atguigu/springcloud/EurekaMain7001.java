package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Auther: hzp
 * @Date: 2020/12/9 15:23
 * @Description:
 */
@SpringBootApplication
@EnableEurekaServer//标识当前为服务注册中心
public class EurekaMain7001 {

    public static void main(String[] args){
        SpringApplication.run(EurekaMain7001.class, args);
    }
}
