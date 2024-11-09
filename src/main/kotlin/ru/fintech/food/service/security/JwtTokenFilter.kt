package ru.fintech.food.service.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.filter.OncePerRequestFilter
import ru.fintech.food.service.user.mapper.UserMapper
import ru.fintech.food.service.user.repository.UserRepository
import ru.fintech.food.service.utils.JwtTokenUtils


class JwtTokenFilter(
    private val jwtTokenUtils: JwtTokenUtils,
    private val userRepository: UserRepository,
) : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        val jwtToken = authHeader?.removePrefix("Bearer ")?.trim()

        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        response.setHeader(
            "Access-Control-Allow-Headers",
            "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With, X-Auth-Token"
        )

        try {
            if (!jwtToken.isNullOrEmpty()) {
                val tokenInRedis = jwtTokenUtils.validateToken(jwtToken)

                if (tokenInRedis) {
                    val email = jwtTokenUtils.getUserEmail(jwtToken)

                    val userDto = UserMapper.toUserDto(
                        userRepository.findUserByEmail(email)
                            .orElseThrow { UsernameNotFoundException("Пользователя с почтой: $email не существует") }
                    )

                    if (SecurityContextHolder.getContext().authentication == null && userDto.isConfirmed) {
                        val token = UsernamePasswordAuthenticationToken(
                            userDto,
                            jwtToken,
                            mutableListOf(SimpleGrantedAuthority("ROLE_${userDto.role}"))
                        )
                        SecurityContextHolder.getContext().authentication = token
                    }
                }
            }
        } catch (e: Exception) {
            log.error("При попытке авторизации что-то пошло не так", e)
        }

        if (request.method == "OPTIONS") {
            response.status = HttpServletResponse.SC_OK
        } else {
            filterChain.doFilter(request, response)
        }
    }
}