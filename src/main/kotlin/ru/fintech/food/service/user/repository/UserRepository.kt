package ru.fintech.food.service.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.fintech.food.service.user.entity.User
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID> {
    fun findUserByEmail(email: String): Optional<User>
    fun existsByEmail(email: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean
}