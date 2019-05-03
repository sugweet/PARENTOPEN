package com.brigeintelligent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @ClassName CloudApp
 * @Description spring-cloud服务发现
 * @Author Sugweet Chen
 * @Date 2019/5/2 20:30
 * @Version 1.0
 **/
@SpringBootApplication
@EnableEurekaServer //注册中心服务端
public class EurekaBootstrapper {
    public static void main(String[] args) {
        SpringApplication.run(EurekaBootstrapper.class, args);
    }
}
