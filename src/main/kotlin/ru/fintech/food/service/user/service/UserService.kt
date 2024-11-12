package ru.fintech.food.service.user.service

import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.fintech.food.service.common.dto.Response
import ru.fintech.food.service.common.exception.BadRequestException
import ru.fintech.food.service.configuration.MailProperties
import ru.fintech.food.service.sender.SenderService
import ru.fintech.food.service.user.dto.token.TokenResponse
import ru.fintech.food.service.user.dto.user.LoginCredentials
import ru.fintech.food.service.user.dto.user.RoleEnum
import ru.fintech.food.service.user.dto.user.UserRegistrationModel
import ru.fintech.food.service.user.entity.RefreshToken
import ru.fintech.food.service.user.entity.User
import ru.fintech.food.service.user.exception.AccountNotConfirmedException
import ru.fintech.food.service.user.exception.UserAlreadyExistsException
import ru.fintech.food.service.user.mapper.UserMapper
import ru.fintech.food.service.user.repository.RefreshTokenRepository
import ru.fintech.food.service.user.repository.UserRepository
import ru.fintech.food.service.utils.JwtTokenUtils

interface UserService {
    fun loadUserByUsername(username: String?): UserDetails
    fun verifyUser(userId: UUID, code: String): CompletableFuture<Response>
    fun validateToken(token: String): Boolean
    fun registerUser(registerModel: UserRegistrationModel): CompletableFuture<Response>
    fun loginUser(loginCredentials: LoginCredentials, refreshToken: RefreshToken): CompletableFuture<TokenResponse>
    fun logoutUser(token: String): CompletableFuture<Response>
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val jwtTokenUtils: JwtTokenUtils,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val senderService: SenderService,
    private val mailProperties: MailProperties
) : UserService, UserDetailsService {
    @Transactional
    override fun verifyUser(userId: UUID, code: String): CompletableFuture<Response> =
        userRepository.findUserById(userId)
            .thenApply { user ->
                if (user == null) {
                    throw UsernameNotFoundException("Пользователь с id: $userId не найден")
                }

                if (user.verificationCode != code)
                    throw BadRequestException("Неверно указанный код подтверждения аккаунта")

                user.isConfirmed = true
                user.verificationCode = null

                userRepository.save(user)

                Response(
                    status = 200,
                    message = "Аккаунт успешно подтвержден",
                )
            }


    override fun validateToken(token: String): Boolean = jwtTokenUtils.validateToken(token)

    @Transactional
    override fun registerUser(registerModel: UserRegistrationModel): CompletableFuture<Response> =
        CompletableFuture.supplyAsync {
            if (userRepository.existsByEmail(registerModel.email))
                throw UserAlreadyExistsException("Пользователь с указанной почтой уже существует")
            else if (userRepository.existsByPhoneNumber(registerModel.phoneNumber))
                throw UserAlreadyExistsException("Пользователь с указанным номером телефона уже существует")

            val user = User(
                email = registerModel.email,
                phoneNumber = registerModel.phoneNumber,
                verificationCode = UUID.randomUUID().toString(),
                password = passwordEncoder.encode(registerModel.password),
                role = RoleEnum.USER
            )

            userRepository.saveAndFlush(user)

            //TODO: Убрать, когда будет настроена почта и добавить асинхронщину
            if (mailProperties.enabled) {
                senderService.proxyMessage(UserMapper.UserDto(user), user.verificationCode!!)
            } else {
                println(user.verificationCode)
            }

            return@supplyAsync Response(
                status = HttpStatus.OK.value(),
                message = "Теперь подтвердите аккаунт"
            )
        }

    @Transactional
    override fun loginUser(
        loginCredentials: LoginCredentials,
        refreshToken: RefreshToken
    ): CompletableFuture<TokenResponse> =
        userRepository.findUserByEmail(loginCredentials.email)
            .thenApply { user ->
                user
                    ?: throw UsernameNotFoundException("Пользователь с почтой: ${loginCredentials.email} не был найден")
            }.thenCompose { user ->
                if (!user.isConfirmed) {
                    throw AccountNotConfirmedException()
                }

                val token = jwtTokenUtils.generateToken(user)

                jwtTokenUtils.saveToken(jwtTokenUtils.getIdFromToken(token))

                CompletableFuture.completedFuture(TokenResponse(token, refreshToken.token))
            }

    @Transactional
    override fun logoutUser(token: String): CompletableFuture<Response> =
        CompletableFuture.supplyAsync {
            val jwtToken = token.removePrefix("Bearer ").trim()
            val tokenId = jwtTokenUtils.getIdFromToken(jwtToken)
            val userId = jwtTokenUtils.getUserId(jwtToken)

            refreshTokenRepository.findByUserId(UUID.fromString(userId)).thenCompose { token ->
                if (token != null) {
                    refreshTokenRepository.delete(token)
                }

                jwtTokenUtils.deleteTokenById(tokenId)

                CompletableFuture.completedFuture(
                    Response(
                        status = HttpStatus.OK.value(),
                        message = "Пользователь успешно вышел с аккаунта"
                    )
                )
            }
        }
            .thenCompose { it }

    override fun loadUserByUsername(username: String?): UserDetails =
        userRepository.findUserByEmail(username!!)
            .thenApply { user ->
                user
                    ?: throw UsernameNotFoundException("Пользователь с почтой: $username не был найден")
            }
            .join()
}