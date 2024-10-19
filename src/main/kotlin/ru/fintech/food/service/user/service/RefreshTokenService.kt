package ru.fintech.food.service.user.service

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.fintech.food.service.common.exception.NotFoundException
import ru.fintech.food.service.configuration.AuthenticationProperties
import ru.fintech.food.service.user.dto.token.RefreshTokenDto
import ru.fintech.food.service.user.dto.token.TokenResponse
import ru.fintech.food.service.user.dto.user.LoginCredentials
import ru.fintech.food.service.user.entity.RefreshToken
import ru.fintech.food.service.user.exception.ExpiredTokenException
import ru.fintech.food.service.user.repository.RedisRepository
import ru.fintech.food.service.user.repository.RefreshTokenRepository
import ru.fintech.food.service.user.repository.UserRepository
import ru.fintech.food.service.utils.JwtTokenUtils
import java.time.Instant
import java.util.*

interface RefreshTokenService {
    fun verifyExpiration(token: RefreshToken): RefreshToken?
    fun checkRefreshToken(credentials: LoginCredentials): RefreshToken
    fun createRefreshToken(email: String): RefreshToken
    fun refreshJwtToken(refreshTokenDto: RefreshTokenDto): ResponseEntity<TokenResponse>
}

@Service
class RefreshTokenServiceImpl(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtTokenUtils: JwtTokenUtils,
    private val userRepository: UserRepository,
    private val properties: AuthenticationProperties,
    private val redisRepository: RedisRepository
) : RefreshTokenService {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun verifyExpiration(token: RefreshToken): RefreshToken? =
        if (token.expiryTime < Instant.now()) {
            refreshTokenRepository.deleteById(token.id)
            null
        } else token


    @Transactional
    override fun checkRefreshToken(credentials: LoginCredentials): RefreshToken {
        val user = userRepository.findUserByEmail(credentials.email)
            .orElseThrow {
                log.debug("Пользователь с почтой: {} не найден", credentials.email)
                NotFoundException("Пользователь с почтой: ${credentials.email} не найден")
            }

        refreshTokenRepository.findByUser(user)
            .map(::verifyExpiration)
            .ifPresent(refreshTokenRepository::delete)

        refreshTokenRepository.flush()

        return createRefreshToken(credentials.email)
    }

    @Transactional
    override fun createRefreshToken(email: String): RefreshToken {
        val refreshToken = RefreshToken(
            user = userRepository.findUserByEmail(email).get(),
            token = UUID.randomUUID().toString(),
            expiryTime = Instant.now().plus(properties.refresh.expiration)
        )

        return refreshTokenRepository.save(refreshToken)
    }

    @Transactional
    override fun refreshJwtToken(refreshTokenDto: RefreshTokenDto) =
        refreshTokenRepository.findByToken(refreshTokenDto.token)
            .orElseThrow { ExpiredTokenException("Срок действия токена истек") }
            .let(::verifyExpiration)?.user
            ?.let { user ->
                val accessToken = jwtTokenUtils.generateToken(user)
                jwtTokenUtils.saveToken(jwtTokenUtils.getIdFromToken(accessToken), "Valid")

                ResponseEntity.ok(TokenResponse(accessToken, refreshTokenDto.token))
            } ?: throw ExpiredTokenException("Срок действия токена истек")
}