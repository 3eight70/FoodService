package ru.fintech.food.service.utils.mail

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import ru.fintech.food.service.configuration.MailProperties

@Configuration
class MailConfiguration(
    private val properties: MailProperties
) {
    @Bean
    fun mailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = properties.host
        mailSender.port = properties.port
        mailSender.username = properties.username
        mailSender.password = properties.password

        mailSender.defaultEncoding = "UTF-8"

        val props = mailSender.javaMailProperties
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.debug"] = "true"

        return mailSender
    }
}