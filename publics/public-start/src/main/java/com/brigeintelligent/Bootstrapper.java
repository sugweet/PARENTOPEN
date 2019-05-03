package com.brigeintelligent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Description 程序启动类
 * @Author Sugweet
 * @Time 2019/4/17 14:25
 */
@SpringBootApplication
@EnableEurekaClient
public class Bootstrapper {
    public static void main(String[] args) {
        SpringApplication.run(Bootstrapper.class, args);
    }
}
