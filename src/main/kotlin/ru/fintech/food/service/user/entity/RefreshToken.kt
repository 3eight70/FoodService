package ru.fintech.food.service.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.jetbrains.annotations.NotNull
import java.time.Instant
import java.util.*

/**
 * Сущность для рефреш токена
 */
@Entity
@Table(name = "t_refresh_tokens")
class RefreshToken(
    /**
     * Идентификатор рефреш токена
     */
    @Id
    val id: UUID = UUID.randomUUID(),
    /**
     * Рефреш токен
     */
    @Column(name = "token", nullable = false)
    val token: String,
    /**
     * Время до которого считается действующим
     */
    @Column(nullable = false)
    val expiryTime: Instant,
    /**
     * Пользователь, которому выдан токен
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    val user: User
)