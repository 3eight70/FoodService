package ru.fintech.food.service.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.fintech.food.service.user.entity.User
import java.util.Optional
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Repository
interface UserRepository : JpaRepository<User, UUID> {
    fun findUserByEmail(email: String): CompletableFuture<User>
    fun existsByEmail(email: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean
    fun findUserById(userId: UUID): CompletableFuture<User>
}