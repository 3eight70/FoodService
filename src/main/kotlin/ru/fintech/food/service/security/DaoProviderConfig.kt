package ru.fintech.food.service.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.crypto.password.PasswordEncoder
import ru.fintech.food.service.user.service.UserServiceImpl

@Configuration
class DaoProviderConfig(
    private val passwordEncoder: PasswordEncoder,
    private val userServiceImpl: UserServiceImpl
) {
    @Bean
    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager =
        configuration.authenticationManager

    @Bean
    fun daoAuthenticationProvider(): DaoAuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setPasswordEncoder(passwordEncoder)
        provider.setUserDetailsService(userServiceImpl)

        return provider
    }
}