package ru.fintech.food.service.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.fintech.food.service.user.dto.user.RoleEnum
import java.util.*

/**
 * Сущность пользователя
 */
@Entity
@Table(name = "t_users")
class User(
    /**
     * Идентификатор
     */
    @Id
    val id: UUID = UUID.randomUUID(),
    /**
     * Электронная почта
     */
    @Column(name = "email", nullable = false, unique = true)
    val email: String,
    /**
     * Номер телефона
     */
    @Column(name = "phone_number", nullable = false, unique = true)
    val phoneNumber: String,
    /**
     * Код подтверждения регистрации
     */
    @Column(name = "verification_code", nullable = true)
    var verificationCode: String?,
    /**
     * Статус подтверждения аккаунта
     */
    @Column(name = "is_confirmed", nullable = false)
    var isConfirmed: Boolean = false,
    /**
     * Пароль
     */
    @Column(name = "password", nullable = false)
    private val password: String,
    /**
     * Роль пользователя
     */
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    val role: RoleEnum
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableListOf(SimpleGrantedAuthority("ROLE_$role"))

    override fun getPassword(): String = password
    override fun getUsername(): String = email
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = isConfirmed
}