package ru.fintech.food.service.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
@EnableConfigurationProperties(AuthenticationProperties::class)
class AuthenticationConfiguration {
}

/**
 * Конфигурация для аутентификации
 */
@ConfigurationProperties("authentication")
class AuthenticationProperties(
    val jwt: JwtProperties,
    val refresh: RefreshProperties
) {
    data class JwtProperties(
        val secret: String,
        val expiration: Duration
    )

    data class RefreshProperties(
        val expiration: Duration
    )
}