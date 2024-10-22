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
import ru.fintech.food.service.user.dto.user.UserDto
import ru.fintech.food.service.user.mapper.UserMapper
import ru.fintech.food.service.user.repository.UserRepository
import ru.fintech.food.service.user.service.UserService
import ru.fintech.food.service.utils.JwtTokenUtils


class JwtTokenFilter(
    private val userService: UserService,
    private val jwtTokenUtils: JwtTokenUtils,
    private val userRepository: UserRepository,
) : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        var authHeader: String? = request.getHeader("Authorization")
        var email: String? = null
        var jwt: String? = null
        var userDto: UserDto? = null

        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        response.setHeader(
            "Access-Control-Allow-Headers",
            "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With, X-Auth-Token"
        )

        if (authHeader != null && authHeader == "Bearer null") {
            authHeader = null
        }

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7)

                val tokenInRedis = userService.validateToken(jwt)

                if (tokenInRedis) {
                    email = jwtTokenUtils.getUserEmail(jwt)
                }
            }

            if (!email.isNullOrEmpty()) {
                userDto = UserMapper.toUserDto(
                    userRepository.findUserByEmail(email)
                        .orElseThrow { UsernameNotFoundException("Пользователя с почтой: $email не существует") }
                )
            }

            if (email != null && SecurityContextHolder.getContext().authentication == null
                && userDto != null && userDto.isConfirmed
            ) {
                val token = UsernamePasswordAuthenticationToken(
                    userDto,
                    jwt,
                    mutableListOf(SimpleGrantedAuthority("ROLE_${userDto.role}"))
                )
                SecurityContextHolder.getContext().authentication = token
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