package com.tm.gogo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().components(components())
                .security(security())
                .info(info());
    }

    private Components components() {
        return new Components().addSecuritySchemes(
                "BearerAuth", new SecurityScheme().type(SecurityScheme.Type.HTTP).in(SecurityScheme.In.HEADER)
                        .name("Authorization")
                        .scheme("Bearer")
                        .bearerFormat("JWT")
        );
    }

    private List<SecurityRequirement> security() {
        return List.of(new SecurityRequirement().addList("BearerAuth"));
    }

    private Info info() {
        return new Info().title("GoGo API")
                .version("1.0.0")
                .description("GoGo API Docs 페이지입니다.");
    }

}
