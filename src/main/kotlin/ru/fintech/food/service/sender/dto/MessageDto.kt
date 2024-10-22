package ru.fintech.food.service.sender.dto

import ru.fintech.food.service.sender.Sender
import ru.fintech.food.service.user.dto.user.UserDto

class MessageDto(
    val user: UserDto,
    val title: String,
    val content: String,
    val senderType: Sender.SenderType
)