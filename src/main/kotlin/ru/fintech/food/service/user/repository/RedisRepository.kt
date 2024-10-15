package ru.fintech.food.service.user.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class RedisRepository(
    private val template: RedisTemplate<String, String>
) {
    fun save(key: String, value: String, expirationTime: Long) {
        template.opsForValue().set(key, value)
        template.expire(key, expirationTime, TimeUnit.MILLISECONDS)
    }

    fun checkTokenByKey(key: String) = template.hasKey(key)

    fun delete(key: String) = template.delete(key)
}