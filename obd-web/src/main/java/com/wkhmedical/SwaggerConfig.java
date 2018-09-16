/*
 * Copyright (C) 2018 TaoXeo. All rights reserved.
 */
package com.wkhmedical;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wkhmedical.security.TUserDetails;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The Class SwaggerConfig.
 *
 * @author Derek
 * @since 1.0, 2018-9-13
 */
@Configuration
@EnableSwagger2
@ConditionalOnWebApplication
@ConditionalOnProperty(name="api-doc", havingValue="true")
public class SwaggerConfig {
    
    /**
     * Creates the rest api.
     *
     * @return the docket
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wkhmedical.web.api"))
                .paths(PathSelectors.any())
                .build().ignoredParameterTypes(HttpSession.class, TUserDetails.class, HttpServletRequest.class);
    }

    /**
     * Api info.
     *
     * @return the api info
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("RESTful API 接口文档")
                .description("OpenApi")
                .version("1.0")
                .build();
    }
}
