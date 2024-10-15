package ru.fintech.food.service.user.service

import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.fintech.food.service.common.dto.Response
import ru.fintech.food.service.user.dto.token.TokenResponse
import ru.fintech.food.service.user.dto.user.LoginCredentials
import ru.fintech.food.service.user.dto.user.UserRegistrationModel
import ru.fintech.food.service.user.entity.RefreshToken
import ru.fintech.food.service.user.entity.User
import ru.fintech.food.service.user.repository.UserRepository

interface UserService {
    fun loadUserByUsername(username: String?): UserDetails
    fun verifyUser(code: String): ResponseEntity<User>
    fun validateToken(token: String): Boolean
    fun registerUser(registerModel: UserRegistrationModel): TokenResponse
    fun loginUser(loginCredentials: LoginCredentials, refreshToken: RefreshToken): TokenResponse
    fun logoutUser(token: String): Response
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
) : UserService, UserDetailsService {
    override fun verifyUser(code: String): ResponseEntity<User> {
        TODO("Not yet implemented")
    }

    override fun validateToken(token: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun registerUser(registerModel: UserRegistrationModel): TokenResponse {
        TODO("Not yet implemented")
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