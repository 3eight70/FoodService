package ru.fintech.food.service.user.service

import java.util.UUID
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    fun verifyUser(userId: UUID, code: String): Response
    fun validateToken(token: String): Boolean
    fun registerUser(registerModel: UserRegistrationModel): Response
    fun loginUser(loginCredentials: LoginCredentials, refreshToken: RefreshToken): TokenResponse
    fun logoutUser(token: String): Response
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
    override fun verifyUser(userId: UUID, code: String): Response {
        val user = userRepository.findById(userId)
            .orElseThrow { UsernameNotFoundException("Пользователь с id: $userId не найден") }

        if (user.verificationCode != code)
            throw BadRequestException("Неверно указанный код подтверждения аккаунта")

        user.isConfirmed = true
        user.verificationCode = null

        userRepository.save(user)

        return Response(
            status = 200,
            message = "Аккаунт успешно подтвержден",
        )
    }

    override fun validateToken(token: String): Boolean = jwtTokenUtils.validateToken(token)

    @OptIn(DelicateCoroutinesApi::class)
    @Transactional
    override fun registerUser(registerModel: UserRegistrationModel): Response {
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

        if (mailProperties.enabled) {
            userRepository.saveAndFlush(user)

            GlobalScope.launch {
                senderService.proxyMessage(UserMapper.UserDto(user), user.verificationCode!!)
            }

            return Response(
                status = HttpStatus.OK.value(),
                message = "Теперь подтвердите аккаунт"
            )
        } else {
            user.verificationCode = null
            user.isConfirmed = true
            userRepository.save(user)
        }

        return Response(
            status = HttpStatus.OK.value(),
            message = "Аккаунт успешно зарегистрирован"
        )
    }

    @Transactional
    override fun loginUser(loginCredentials: LoginCredentials, refreshToken: RefreshToken): TokenResponse {
        val user = userRepository.findUserByEmail(loginCredentials.email)
            .orElseThrow { UsernameNotFoundException("Пользователь с почтой: ${loginCredentials.email} не был найден") }

        if (!user.isConfirmed) {
            throw AccountNotConfirmedException()
        }

        val token = jwtTokenUtils.generateToken(user)

        jwtTokenUtils.saveToken(jwtTokenUtils.getIdFromToken(token))

        return TokenResponse(token, refreshToken.token)
    }

    @Transactional
    override fun logoutUser(token: String): Response {
        val jwtToken = token.removePrefix("Bearer ").trim()
        val tokenId = jwtTokenUtils.getIdFromToken(jwtToken)
        val userId = jwtTokenUtils.getUserId(jwtToken)

        val refreshToken = refreshTokenRepository.findByUserId(UUID.fromString(userId))

        refreshToken.ifPresent { value ->
            refreshTokenRepository.delete(value)
        }

        jwtTokenUtils.deleteTokenById(tokenId)

        return Response(
            status = HttpStatus.OK.value(),
            message = "Пользователь успешно вышел с аккаунта",
        )
    }

    override fun loadUserByUsername(username: String?): UserDetails =
        userRepository.findUserByEmail(username!!)
            .orElseThrow { UsernameNotFoundException("Пользователь с почтой: $username не был найден") }

}