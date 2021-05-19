package com.springboot.bishe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * ClassName: SwaggerConfig
 * Package: com.springboot.bishe.config
 *
 * @Description:
 * @Date: 2021/4/22 8:24
 * @author: 浪漫
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    //  创建api
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.springboot.bishe.service"))
                .paths(PathSelectors.any())
                .build();
    }


    // api 属性
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("eboot-api文档")
                .description("更多信息，请访问https://www.jianshu.com/u/3979cb11f079")
                .termsOfServiceUrl("https://gitee.com/QuanZhanZhiLu/easy-boot")
                .version("1.0")
                .build();
    }


}
