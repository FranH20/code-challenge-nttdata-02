package com.nttdata.fhuichic.module.temperature.infrastructure.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OpenApiConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Temperature API")
                        .description("Temperature API NTTData N°02")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Franklin Huichi")));
    }

}
