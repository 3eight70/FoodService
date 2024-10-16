package ru.fintech.food.service.sender

import ru.fintech.food.service.sender.dto.MessageDto

interface Sender {
    fun sendMessage(message: MessageDto)
    fun getType(): SenderType

    sealed interface SenderType {
        data object Email : SenderType
    }
}