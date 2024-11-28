package ru.fintech.food.service.user.repository

import java.util.Optional
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.fintech.food.service.user.dto.user.RoleEnum
import ru.fintech.food.service.user.entity.User

@Repository
interface UserRepository : JpaRepository<User, UUID> {
    fun findUserByEmail(email: String): Optional<User>
    fun existsByEmail(email: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean
    fun findAllByRole(role: RoleEnum): List<User>
}