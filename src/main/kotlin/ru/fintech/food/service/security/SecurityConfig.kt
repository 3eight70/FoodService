package ru.fintech.food.service.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import ru.fintech.food.service.user.service.UserServiceImpl

@EnableWebSecurity
@Configuration
class SecurityConfig(
    private val userServiceImpl: UserServiceImpl
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { csrf -> csrf.disable() }
            .cors { cors -> cors.disable() }
            .authorizeHttpRequests { requests ->
                requests
                    .anyRequest().permitAll()
            }
            .exceptionHandling { exception ->
                exception
                    .accessDeniedHandler(accessDeniedHandler())
                    .authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            }
            .sessionManagement { configurer ->
                configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager =
        configuration.authenticationManager

    @Bean
    fun daoAuthenticationProvider(): DaoAuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setPasswordEncoder(passwordEncoder())
        provider.setUserDetailsService(userServiceImpl)

        return provider
    }

    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.allowedOrigins = mutableListOf("*")
        config.allowedHeaders = mutableListOf("*")
        config.allowedMethods = mutableListOf("*")
        source.registerCorsConfiguration("/**", config)

        return CorsFilter(source)
    }

    private fun accessDeniedHandler(): AccessDeniedHandler {
        return AccessDeniedHandler { _, response, _ ->
            response.contentType = "application/json"
            response.characterEncoding = "UTF-8"
            response.status = HttpStatus.FORBIDDEN.value()
            response.writer.write("{\"status\": \"403\", \"message\": \"У вас нет прав доступа\"}")
            response.writer.flush()
        }
    }
}