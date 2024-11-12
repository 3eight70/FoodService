package ru.fintech.food.service.user.service

import java.time.Instant
import java.util.UUID
import java.util.concurrent.CompletableFuture
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
import ru.fintech.food.service.user.repository.RefreshTokenRepository
import ru.fintech.food.service.user.repository.UserRepository
import ru.fintech.food.service.utils.JwtTokenUtils

interface RefreshTokenService {
    fun verifyExpiration(token: RefreshToken): CompletableFuture<RefreshToken>
    fun checkRefreshToken(credentials: LoginCredentials): CompletableFuture<RefreshToken>
    fun createRefreshToken(email: String): CompletableFuture<RefreshToken>
    fun refreshJwtToken(refreshTokenDto: RefreshTokenDto): CompletableFuture<ResponseEntity<TokenResponse>>
}

@Service
class RefreshTokenServiceImpl(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtTokenUtils: JwtTokenUtils,
    private val userRepository: UserRepository,
    private val properties: AuthenticationProperties
) : RefreshTokenService {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun verifyExpiration(token: RefreshToken): CompletableFuture<RefreshToken> =
        CompletableFuture.supplyAsync {
            if (token.expiryTime < Instant.now()) {
                refreshTokenRepository.deleteById(token.id)
                null
            } else token
        }


    @Transactional
    override fun checkRefreshToken(credentials: LoginCredentials): CompletableFuture<RefreshToken> =
        userRepository.findUserByEmail(credentials.email)
            .thenCompose { user ->
                if (user == null) {
                    log.debug("Пользователь с почтой: {} не найден", credentials.email)
                    throw NotFoundException("Пользователь с почтой: ${credentials.email} не найден")
                }

                refreshTokenRepository.findByUser(user)
                    .thenCompose { token ->
                        verifyExpiration(token)
                            .thenCompose { verifiedToken ->
                                if (token != null) {
                                    CompletableFuture.supplyAsync {
                                        refreshTokenRepository.delete(verifiedToken)
                                    }
                                } else {
                                    CompletableFuture.completedFuture(null)
                                }
                            }
                    }
                    .thenCompose { _ ->
                        refreshTokenRepository.flush()
                        createRefreshToken(credentials.email)
                    }
            }

    @Transactional
    override fun createRefreshToken(email: String): CompletableFuture<RefreshToken> =
        userRepository.findUserByEmail(email)
            .thenCompose { user ->
                val refreshToken = RefreshToken(
                    user = user,
                    token = UUID.randomUUID().toString(),
                    expiryTime = Instant.now().plus(properties.refresh.expiration)
                )

                CompletableFuture.supplyAsync {
                    refreshTokenRepository.save(refreshToken)
                }
            }


    @Transactional
    override fun refreshJwtToken(refreshTokenDto: RefreshTokenDto): CompletableFuture<ResponseEntity<TokenResponse>> =
        refreshTokenRepository.findByToken(refreshTokenDto.token)
            .thenCompose { refreshToken ->
                if (refreshToken == null) {
                    CompletableFuture.failedFuture(ExpiredTokenException("Срок действия токена истек"))
                } else {
                    verifyExpiration(refreshToken)
                        .thenCompose { verifiedToken ->
                            val user = verifiedToken.user
                            val accessToken = jwtTokenUtils.generateToken(user)
                            jwtTokenUtils.saveToken(jwtTokenUtils.getIdFromToken(accessToken))

                            CompletableFuture.completedFuture(
                                ResponseEntity.ok(
                                    TokenResponse(
                                        accessToken = accessToken,
                                        refreshToken = refreshTokenDto.token
                                    )
                                )
                            )
                        } ?: CompletableFuture.failedFuture(ExpiredTokenException("Срок действия токена истек"))
                }
            }
}