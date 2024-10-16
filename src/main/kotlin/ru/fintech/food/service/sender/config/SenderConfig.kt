package ru.fintech.food.service.sender.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.fintech.food.service.sender.Sender

@Configuration
class SenderConfig(
    private val senders: List<Sender>
) {

    @Bean
    fun senderMap(): Map<Sender.SenderType, Sender> {
        val map = hashMapOf<Sender.SenderType, Sender>()

        senders.forEach {
            map[it.getType()] = it
        }

        return map
    }
}