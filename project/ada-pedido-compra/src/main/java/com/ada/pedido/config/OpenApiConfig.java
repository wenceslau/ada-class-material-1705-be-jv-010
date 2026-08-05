package com.ada.pedido.config;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import jakarta.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title = "API Pedido de Compra",
                version = "1.0-SNAPSHOT",
                description = "API REST para gerenciamento de clientes, produtos e pedidos com autenticação JWT.",
                contact = @Contact(name = "ADA Course", email = "suporte@ada.com")
        ),
        security = @SecurityRequirement(name = "jwt")
)
@SecurityScheme(
        securitySchemeName = "jwt",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Autenticação via Token JWT. Insira o token gerado no endpoint /login sem o prefixo 'Bearer '."
)
public class OpenApiConfig extends Application {
}
