package com.atguigu.myrule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: hzp
 * @Date: 2020/12/16 16:45
 * @Description: 由于Ribbon 官方文档提示，当需要自定义负载均衡规则时，最好不要将该规则置于@ComponentScan所扫描的当前包及以下所有子包中，否则所有被扫描到的客户端
 * 将共用该自定义规则，又因为@SpringBootApplication中包含了@ComponentScan，所以该自定义规则不应和主启动类OrderMain80在同一个包下.
 * 负载均衡算法：rest接口第几次请求数 % 服务器集群总数 = 实际调用服务器位置下标。每次服务重启动后rest接口计数从1开始
 */
@Configuration
public class MySelfRule {

    @Bean
    public IRule myRule(){
        return new RandomRule();//定义为随机
    }
}

