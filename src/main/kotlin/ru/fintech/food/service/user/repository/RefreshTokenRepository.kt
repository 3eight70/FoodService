package ru.fintech.food.service.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.fintech.food.service.user.entity.RefreshToken
import ru.fintech.food.service.user.entity.User
import java.util.*

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, UUID> {
    fun findByUser(user: User): Optional<RefreshToken>
    fun findByToken(token: String): Optional<RefreshToken>
    fun findByUserId(userId: UUID): Optional<RefreshToken>
}