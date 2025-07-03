package com.example.gamecatalog.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {

    @Autowired
    private BuildProperties buildProperties;

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi
                .builder()
                .group("Game Catalog API")
                .pathsToMatch("/api/**")
                .addOpenApiCustomizer(statusApiCustomizer())
                .build();
    }


    private OpenApiCustomizer statusApiCustomizer() {
        return openAPI -> openAPI
                .info(new Info()
                        .title("Springboot Game Catalog Service")
                        .description("This is a Spring Boot RESTful service using OpenAPI")
                        .version(buildProperties.getVersion())
                        .contact(new Contact()
                                .name("Filipe Melim")
                                .email("filipedom@hotmail.com")));
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("Catalog Service API").description(
                        "This is a Spring Boot RESTful service using OpenAPI 3."));
    }
}