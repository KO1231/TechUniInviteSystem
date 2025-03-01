package org.techuni.TechUniInviteSystem.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // セキュリティスキームの定義
        SecurityScheme securityScheme = new SecurityScheme() //
                .type(SecurityScheme.Type.HTTP) //
                .scheme("bearer") //
                .bearerFormat("JWT") //
                .in(SecurityScheme.In.HEADER) //
                .name("Authorization");

        // セキュリティリファレンスの定義
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI() //
                .addSecurityItem(securityRequirement) //
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme));
    }
}
