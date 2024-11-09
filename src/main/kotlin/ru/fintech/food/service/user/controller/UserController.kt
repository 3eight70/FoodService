package ru.fintech.food.service.user.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.fintech.food.service.common.dto.Response
import ru.fintech.food.service.user.dto.token.TokenResponse
import ru.fintech.food.service.user.dto.user.LoginCredentials
import ru.fintech.food.service.user.dto.user.UserRegistrationModel
import ru.fintech.food.service.user.service.RefreshTokenService
import ru.fintech.food.service.user.service.UserService

@RestController
@Tag(name = "Пользователь", description = "Отвечает за работу с пользователями")
@RequestMapping("/v1/user")
class UserController(
    private val userService: UserService,
    private val refreshTokenService: RefreshTokenService,
    private val authenticationManager: AuthenticationManager
) {

    @Operation(
        summary = "Авторизация",
        description = "Позволяет пользователю залогониться"
    )
    @PostMapping("/login")
    fun loginUser(@Valid @RequestBody loginCredentials: LoginCredentials): ResponseEntity<TokenResponse> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginCredentials.email,
                loginCredentials.password
            )
        )

        if (authentication.isAuthenticated) {
            val refreshToken = refreshTokenService.checkRefreshToken(loginCredentials)

            return ResponseEntity.ok(userService.loginUser(loginCredentials, refreshToken))
        }

        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @Operation(
        summary = "Регистрация пользователя",
        description = "Позволяет пользователю зарегистрироваться"
    )
    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody registerModel: UserRegistrationModel): ResponseEntity<Response> {
        return ResponseEntity.ok(userService.registerUser(registerModel))
    }

    @Operation(
        summary = "Подтверждение аккаунта",
        description = "Позволяет пользователю подтвердить аккаунт"
    )
    @PostMapping("/verify")
    fun verifyUser(
        @RequestParam("userId") @Parameter(description = "Идентификатор пользователя") userId: UUID,
        @RequestParam("code") @Parameter(description = "Код подтверждения аккаунта") code: String
    ) = ResponseEntity.ok(userService.verifyUser(userId, code))

    @Operation(
        summary = "Валидация access токена",
        description = "Позволяет проверить токен на валидность"
    )
    @GetMapping("/validate")
    fun validateToken(@RequestParam("token") @Parameter(description = "access токен") token: String) =
        ResponseEntity.ok(userService.validateToken(token))

    @Operation(
        summary = "Завершение текущей сессии",
        description = "Позволяет пользователю обнулить текущую сессию"
    )
    @PostMapping("/logout")
    fun logoutUser(@RequestHeader(value = "Authorization") token: String) =
        ResponseEntity.ok(userService.logoutUser(token))
}