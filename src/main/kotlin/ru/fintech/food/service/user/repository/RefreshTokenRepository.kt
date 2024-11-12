package ru.fintech.food.service.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.fintech.food.service.user.entity.RefreshToken
import ru.fintech.food.service.user.entity.User
import java.util.Optional
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, UUID> {
    fun findByUser(user: User): CompletableFuture<RefreshToken>
    fun findByToken(token: String): CompletableFuture<RefreshToken>
    fun findByUserId(userId: UUID): CompletableFuture<RefreshToken>
}