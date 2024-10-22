package ru.fintech.food.service.user.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.fintech.food.service.user.dto.token.RefreshTokenDto
import ru.fintech.food.service.user.dto.token.TokenResponse
import ru.fintech.food.service.user.service.RefreshTokenService

@RestController
@Tag(name = "Refresh токен", description = "Позволяет обновлять access токен с использованием refresh токена")
@RequestMapping("/v1")
class RefreshTokenController(
    private val refreshTokenService: RefreshTokenService
) {
    @PostMapping("/refresh-token")
    @Operation(
        summary = "Обновление access токена",
        description = "Позволяет пользователю обновить access токен при помощи refresh токена"
    )
    fun refreshToken(@RequestBody refreshTokenDto: RefreshTokenDto): ResponseEntity<TokenResponse> =
        refreshTokenService.refreshJwtToken(refreshTokenDto)
}