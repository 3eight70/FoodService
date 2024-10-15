package ru.fintech.food.service.user.dto.user

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Schema(description = "dto для авторизации пользователя")
class LoginCredentials (
    @Schema(description = "Почта пользователя", example = "example@mail.ru")
    @NotNull(message = "Почта должна быть указана")
    @Pattern(regexp = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+", message = "Неверный адрес электронной почты")
    @Size(min = 1, message = "Минимальная длина электронной почты не менее 1 символа")
    val email: String,

    @NotNull(message = "Пароль должен быть указан")
    @Size(min = 1, message = "Минимальная длина пароля равна 1")
    @Schema(description = "Пароль пользователя", example = "qwerty12345")
    val password: String
)