package ru.fintech.food.service.user.dto.user

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Schema(description = "dto для регистрации пользователя")
class UserRegistrationModel(
    @Schema(description = "Почта пользователя", example = "example@mail.ru")
    @field:NotNull(message = "Почта должна быть указана")
    @field:Pattern(regexp = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+", message = "Неверный адрес электронной почты")
    @field:Size(min = 1, message = "Минимальная длина электронной почты не менее 1 символа")
    val email: String,
    @field:Pattern(
        regexp = "^\\+7 \\(\\d{3}\\) \\d{3}-\\d{2}-\\d{2}$",
        message = "Телефон должен быть указан в формате +7 (xxx) xxx-xx-xx"
    )
    @field:NotNull(message = "Номер телефона должен быть указан")
    @Schema(description = "Телефонный номер", example = "+7 (777) 777-77-77")
    val phoneNumber: String,

    @field:Pattern(regexp = "^(?=.*\\d).{6,}$", message = "Пароль должен содержать не менее 6 символов и 1 цифры")
    @field:NotNull(message = "Пароль должен быть указан")
    @Schema(description = "Пароль пользователя", example = "qwerty12345")
    var password: String
)