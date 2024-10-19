package ru.fintech.food.service.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(MailProperties::class)
class MailConfiguration

/**
 * Конфигурация для рассылки писем
 */
@ConfigurationProperties("mail")
class MailProperties(
    val enabled: Boolean = false,
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val senderName: String = "sender",
    val urlToWhere: String
)