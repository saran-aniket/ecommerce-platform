package com.learn.usermicroservice.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "User-Service",
                description = "User service APIs to perform CRUD operations on Application Users",
                version = "1.0",
                contact = @Contact(
                        name = "Aniket Sharan",
                        email = "codenovice880@gmail.com"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:4002",
                        description = "Development Server"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecuritySchemes(
        value = @SecurityScheme(
                name = "bearerAuth",
                description = "Authentication using jwt bearer token",
                type = SecuritySchemeType.HTTP,
                scheme = "bearer",
                bearerFormat = "JWT",
                in = SecuritySchemeIn.HEADER
        ))
public class OpenApiConfig {
}
