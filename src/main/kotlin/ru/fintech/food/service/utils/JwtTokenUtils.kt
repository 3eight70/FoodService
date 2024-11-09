package ru.fintech.food.service.utils

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import ru.fintech.food.service.configuration.AuthenticationProperties
import ru.fintech.food.service.user.entity.User
import ru.fintech.food.service.user.repository.RedisTokenRepository
import java.util.*

@Component
class JwtTokenUtils(
    private val properties: AuthenticationProperties,
    private val redisTokenRepository: RedisTokenRepository
) {
    private val secretKey = Base64.getDecoder().decode(properties.jwt.secret)
    private val key = Keys.hmacShaKeyFor(secretKey)

    fun generateToken(user: User): String {
        val claims = hashMapOf<String, Any>()
        claims["role"] = user.role

        val issuedDate = Date()
        val expiredDate = Date(issuedDate.time + properties.jwt.expiration.toMillis())
        val tokenId = UUID.randomUUID()

        return Jwts.builder()
            .claims(claims)
            .subject(user.email)
            .claim("userId", user.id.toString())
            .id(tokenId.toString())
            .issuedAt(issuedDate)
            .expiration(expiredDate)
            .signWith(key)
            .compact()
    }

    fun saveToken(key: String) = redisTokenRepository.saveToken(key, properties.jwt.expiration.toMillis())

    fun validateToken(token: String) =
        try {
            redisTokenRepository.checkTokenByKey(getIdFromToken(token))
        } catch (e: ExpiredJwtException) {
            false
        }


    fun getIdFromToken(token: String): String = getAllClaims(token).id

    fun getUserId(token: String) = getAllClaims(token)["userId"] as String

    fun getUserEmail(token: String): String = getAllClaims(token).subject

    fun deleteTokenById(tokenId: String) = redisTokenRepository.deleteToken(tokenId)

    private fun getAllClaims(token: String) =
        Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
}