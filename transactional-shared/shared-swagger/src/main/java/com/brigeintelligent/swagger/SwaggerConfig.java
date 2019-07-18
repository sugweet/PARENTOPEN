package com.brigeintelligent.swagger;

import com.brigeintelligent.swagger.api.SharedApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description：swagger配置bean
 * @Author：Sugweet
 * @Time：2019/4/28 17:20
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket Shared() {
        return SharedApi.swagger();
    }

}
