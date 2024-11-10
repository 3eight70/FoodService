package ru.fintech.food.service.user.dto.token

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "dto для обновления токена")
class RefreshTokenDto(
    @field:Schema(description = "refresh токен")
    val token: String
)