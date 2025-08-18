package com.learn.authenticationmicroservice.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Auth-Service",
                description = "Auth service APIs to perform authentication operations",
                version = "1.0",
                contact = @Contact(
                        name = "Aniket Sharan",
                        email = "codenovice880@gmail.com"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:4003",
                        description = "Development Server"
                )
        }
)
public class OpenApiConfig {
}
