package ru.fintech.food.service.user.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Entity(name = "t_users")
class User(
    @Id
    val id: UUID = UUID.randomUUID(),
    val email: String,
    val phoneNumber: String,
    val verificationCode: String,
    val isConfirmed: Boolean = false,
    val password: String
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableListOf(SimpleGrantedAuthority("ROLE_USER"))

    override fun getPassword(): String = password

    override fun getUsername(): String = email
}