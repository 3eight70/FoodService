package ru.fintech.food.service.user.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class RedisTokenRepository(
    private val template: RedisTemplate<String, String>
) {
    companion object {
        private const val TOKEN_KEY_PREFIX = "auth:token:"
    }

    fun saveToken(tokenId: String, expirationTime: Long) {
        val key = getTokenKey(tokenId)
        template.opsForValue().set(key, "V")
        template.expire(key, expirationTime, TimeUnit.MILLISECONDS)
    }

    fun checkTokenByKey(tokenId: String) = template.hasKey(getTokenKey(tokenId))

    fun deleteToken(tokenId: String) = template.delete(getTokenKey(tokenId))

    private fun getTokenKey(tokenId: String) = TOKEN_KEY_PREFIX + tokenId
}