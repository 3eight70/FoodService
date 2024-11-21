package ru.fintech.food.service.common

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import ru.fintech.food.service.common.SwaggerConfig.Companion.BEARER_AUTH

@OpenAPIDefinition
@SecurityScheme(
    name = BEARER_AUTH,
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
class SwaggerConfig {
    companion object {
        const val BEARER_AUTH = "bearerAuth"
    }
}