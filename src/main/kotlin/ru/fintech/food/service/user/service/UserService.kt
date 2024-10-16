package ru.fintech.food.service.user.service

import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.fintech.food.service.common.dto.Response
import ru.fintech.food.service.common.exception.BadRequestException
import ru.fintech.food.service.sender.Sender
import ru.fintech.food.service.sender.SenderService
import ru.fintech.food.service.sender.dto.MessageDto
import ru.fintech.food.service.user.dto.token.TokenResponse
import ru.fintech.food.service.user.dto.user.LoginCredentials
import ru.fintech.food.service.user.dto.user.RoleEnum
import ru.fintech.food.service.user.dto.user.UserDto
import ru.fintech.food.service.user.dto.user.UserRegistrationModel
import ru.fintech.food.service.user.entity.RefreshToken
import ru.fintech.food.service.user.entity.User
import ru.fintech.food.service.user.exception.UserAlreadyExistsException
import ru.fintech.food.service.user.mapper.UserMapper
import ru.fintech.food.service.user.repository.UserRepository
import ru.fintech.food.service.utils.JwtTokenUtils
import java.util.*

interface UserService {
    fun loadUserByUsername(username: String?): UserDetails
    fun verifyUser(userDto: UserDto, code: String): ResponseEntity<Response>
    fun validateToken(token: String): Boolean
    fun registerUser(registerModel: UserRegistrationModel): TokenResponse
    fun loginUser(loginCredentials: LoginCredentials, refreshToken: RefreshToken): TokenResponse
    fun logoutUser(token: String): Response
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val jwtTokenUtils: JwtTokenUtils,
    private val refreshTokenService: RefreshTokenService,
    private val senderService: SenderService
) : UserService, UserDetailsService {
    @Transactional
    override fun verifyUser(userDto: UserDto, code: String): ResponseEntity<Response> {
        val user = userRepository.findById(userDto.id).get()

        if (user.verificationCode != code)
            throw BadRequestException("Неверно указанный код подтверждения аккаунта")

        user.isConfirmed = true
        user.verificationCode = null

        userRepository.save(user)

        return ResponseEntity.ok(
            Response(
                status = 200,
                message = "Аккаунт успешно подтвержден"
            )
        )
    }

    override fun validateToken(token: String): Boolean {
        TODO("Not yet implemented")
    }

    @Transactional
    override fun registerUser(registerModel: UserRegistrationModel): TokenResponse {
        if (userRepository.existsByEmail(registerModel.email))
            throw UserAlreadyExistsException("Пользователь с указанной почтой уже существует")
        else if (userRepository.existsByPhoneNumber(registerModel.phoneNumber))
            throw UserAlreadyExistsException("Пользователь с указанным номером телефона уже существует")

        val user = User(
            email = registerModel.email,
            phoneNumber = registerModel.phoneNumber,
            verificationCode = UUID.randomUUID().toString(),
            password = registerModel.password,
            role = RoleEnum.USER
        )

        userRepository.saveAndFlush(user)

        senderService.proxyMessage(createMessageDto(UserMapper.toUserDto(user)))

        val token = jwtTokenUtils.generateToken(user)
        jwtTokenUtils.saveToken(jwtTokenUtils.getIdFromToken(token), "Valid")
        val refresh = refreshTokenService.createRefreshToken(user.email)

        return TokenResponse(
            accessToken = token,
            refreshToken = refresh.token
        )
    }

    private fun createMessageDto(userDto: UserDto): MessageDto {
        val content =
            """
                Эй,<br>
                Пожалуйста перейдите по ссылке ниже для подтверждения регистрации:<br>
                <h3><a href=\"[[URL]]\" target=\"_self\">ПОДТВЕРДИ МЕНЯ</a></h3>
                Спасибо ;)
            """

        return MessageDto(
            user = userDto,
            title = "Подтверждение почты",
            content = content,
            senderType = Sender.SenderType.Email
        )
    }

    override fun loginUser(loginCredentials: LoginCredentials, refreshToken: RefreshToken): TokenResponse {
        TODO("Not yet implemented")
    }

    override fun logoutUser(token: String): Response {
        TODO("Not yet implemented")
    }

    override fun loadUserByUsername(username: String?): UserDetails =
        userRepository.findUserByEmail(username!!)
            .orElseThrow { UsernameNotFoundException("Пользователь с почтой: $username не был найден") }


}