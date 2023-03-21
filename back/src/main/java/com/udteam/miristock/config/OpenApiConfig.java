package com.udteam.miristock.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info().title("MiriStock API").version("Ver.1.1.0")
                .description("MiriStock API문서 입니다.")
//                .termsOfService("http://swagger.io/terms/")\
                .contact(new Contact().name("MiriStock").url("http://miristockserverlinkdummyurl.com").email("MiriStock@miri.com"))
                .license(new License().name("MIT Licence").url("https://opensource.org/licenses/MIT"));

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }

}
