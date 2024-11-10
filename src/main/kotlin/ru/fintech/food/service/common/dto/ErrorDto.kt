package ru.fintech.food.service.common.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "dto для ошибок валидации")
class ErrorDto(
    @field:Schema(description = "Поле ошибки", example = "login")
    @field:JsonProperty("field")
    private val field: String,
    @field:Schema(description = "Сообщение ошибки", example = "Логин должен быть указан")
    @field:JsonProperty("message")
    private val message: String?
)