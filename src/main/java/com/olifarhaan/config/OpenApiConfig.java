package com.olifarhaan.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {
    @Value("${app.url.production}")
    private String productionUrl;

    @Value("${app.url.local}")
    private String localUrl;

    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    @Value("${app.description}")
    private String appDescription;

    @Value("${app.author.name}")
    private String appAuthorName;

    @Value("${app.author.url}")
    private String appAuthorUrl;

    @Value("${app.author.email}")
    private String appAuthorEmail;

    @Bean
    public OpenAPI usersMicroserviceOpenAPI() {
        Info apiInfo = new Info()
                .title(appName)
                .description(appDescription)
                .version(appVersion)
                .contact(new Contact()
                        .name(appAuthorName)
                        .url(appAuthorUrl)
                        .email(appAuthorEmail));

        List<Server> servers = List.of(new Server()
                .url(productionUrl)
                .description("Production Server"),
                new Server()
                        .url(localUrl)
                        .description("Local Server"));
        return new OpenAPI()
                .info(apiInfo)
                .servers(servers)
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes("bearerAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER)
                        .name("Authorization")
                        .description("Enter JWT Bearer token **_only_**")));
    }
}
