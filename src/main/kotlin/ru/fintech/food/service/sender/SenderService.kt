package ru.fintech.food.service.sender

import org.springframework.stereotype.Service
import ru.fintech.food.service.sender.dto.MessageDto

interface SenderService {
    fun proxyMessage(messageDto: MessageDto)
}

@Service
class SenderServiceImpl(
    private val senderMap: Map<Sender.SenderType, Sender>
) : SenderService {
    override fun proxyMessage(messageDto: MessageDto) {
        senderMap[messageDto.senderType]!!.sendMessage(messageDto)
    }
}