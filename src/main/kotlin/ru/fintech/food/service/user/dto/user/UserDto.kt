package ru.fintech.food.service.user.dto.user

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(description = "dto пользователя")
class UserDto(
    @Schema(description = "Идентификатор пользователя", example = "3c03e1de-2177-43b2-aecb-aa4e9ff66a23")
    val id: UUID,
    @Schema(description = "Электронная почта пользователя", example = "example@gmail.com")
    val email: String,
    @Schema(description = "Номер телефона пользователя", example = "+7 (999) 999-99-99")
    val phoneNumber: String,
    @Schema(description = "Роль пользователя", example = "USER")
    val role: RoleEnum,
    @Schema(description = "Статус подтверждения аккаунта", example = "true")
    val isConfirmed: Boolean
)