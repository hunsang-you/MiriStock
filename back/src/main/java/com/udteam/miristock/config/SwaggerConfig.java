package com.udteam.miristock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;
import java.util.Set;


@Configuration
@EnableSwagger2
public class SwaggerConfig {
//	Swagger-UI 2.x 확인
//	http://localhost:8080/{your-app-root}/swagger-ui.html
//	Swagger-UI 3.x 확인
//	http://localhost:8080/{your-app-root}/swagger-ui/index.html

        private String version = "V1";
        private final String title = "MiriStock API " + version;

        @Bean
        public Docket api() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .consumes(getConsumeContentTypes())
                    .produces(getProduceContentTypes())
                    .apiInfo(apiInfo())
                    .groupName(version).select()
                    .apis(RequestHandlerSelectors.basePackage("com.udteam.miristock.controller")) // RestController만 설정 가능
                    .paths(PathSelectors.any()).build()
                    .useDefaultResponseMessages(false);
        }

        private Set<String> getConsumeContentTypes() {
            Set<String> consumes = new HashSet<>();
            consumes.add("application/json;charset=UTF-8");
            consumes.add("application/x-www-form-urlencoded");
            return consumes;
        }

        private Set<String> getProduceContentTypes() {
            Set<String> produces = new HashSet<>();
            produces.add("application/json;charset=UTF-8");
            return produces;
        }

        private ApiInfo apiInfo() {
            return new ApiInfoBuilder().title(title)
                    .description("<h3>MiriStock API Reference for Developers</h3>Swagger를 이용한 Board API<br><img src=\"/assets/miri_logo.png\" width=\"150\">")
                    .contact(new Contact("MiriStock", "I8B111.p.ssafy.io", "MiriStock@miri.com"))
                    .version("1.0").build();
        }

}
