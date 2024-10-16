package ru.fintech.food.service.sender.impl

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import ru.fintech.food.service.configuration.MailProperties
import ru.fintech.food.service.sender.Sender
import ru.fintech.food.service.sender.dto.MessageDto

@Component
class MailSender(
    private val sender: JavaMailSender,
    private val properties: MailProperties
) : Sender {
    override fun sendMessage(messageDto: MessageDto) {
        val message = sender.createMimeMessage()
        val helper = MimeMessageHelper(message)

        helper.setFrom(properties.username, properties.senderName)
        helper.setTo(messageDto.user.email)
        helper.setSubject(messageDto.title)
        helper.setText(messageDto.content, true)
    }

    override fun getType(): Sender.SenderType = Sender.SenderType.Email
}