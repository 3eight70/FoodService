package ru.fintech.food.service.user.service

import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import ru.fintech.food.service.user.entity.User
import ru.fintech.food.service.user.repository.UserRepository

interface UserService {
    fun loadByUsername(email: String): User
    fun verifyUser(code: String): ResponseEntity<User>
    fun validateToken(token: String): Boolean
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
) : UserService, UserDetailsService {

}