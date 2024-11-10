package ru.fintech.food.service.user.dto.token

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Ответ с токенами")
class TokenResponse(
    @field:Schema(description = "jwt токен")
    val accessToken: String,
    @field:Schema(description = "refresh токен")
    val refreshToken: String
)