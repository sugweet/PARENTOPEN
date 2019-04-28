package com.brigeintelligent.swagger.api;

import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Description：shared配置
 * @Author：Sugweet
 * @Time：2019/4/28 17:23
 */
public class SharedApi {
    public static Docket swagger() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("Shared")
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.brigeintelligent"))
                .paths(PathSelectors.regex("/api/.*"))
                .build()
                .apiInfo(apiInfo());
    }

    private static ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("在线接口文档")
                .description("本文档属于测试文档")
                .termsOfServiceUrl("")
                .version("2019/4/28")
                .build();
    }
}
