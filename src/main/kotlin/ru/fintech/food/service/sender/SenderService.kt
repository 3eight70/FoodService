package ru.fintech.food.service.sender

import org.springframework.stereotype.Service
import ru.fintech.food.service.configuration.MailProperties
import ru.fintech.food.service.sender.dto.MessageDto
import ru.fintech.food.service.user.dto.user.UserDto

interface SenderService {
    fun proxyMessage(userDto: UserDto, verificationCode: String)
}

@Service
class SenderServiceImpl(
    private val senderMap: Map<Sender.SenderType, Sender>,
    private val mailProperties: MailProperties
) : SenderService {
    override fun proxyMessage(userDto: UserDto, verificationCode: String) {
        val messageDto = createMessageDto(userDto, verificationCode)

        senderMap[messageDto.senderType]!!.sendMessage(messageDto)
    }

    private fun createMessageDto(userDto: UserDto, verificationCode: String): MessageDto {
        val content =
            """
                Эй,<br>
                Пожалуйста перейдите по ссылке ниже для подтверждения регистрации:<br>
                <h3><a href=\"${mailProperties.urlToWhere}?userId=${userDto.id}?
                verificationCode=$verificationCode\" target=\"_self\">ПОДТВЕРДИ МЕНЯ</a></h3>
                Спасибо ;)
            """

        return MessageDto(
            user = userDto,
            title = "Подтверждение почты",
            content = content,
            senderType = Sender.SenderType.Email
        )
    }
}