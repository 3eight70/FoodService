package ru.fintech.food.service.restaurant.dto.restaurant

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(description = "dto для создания/обновления ресторанов")
class RestaurantRequestDto(
    @field:Schema(description = "Название ресторана", example = "Пельменная")
    @field:NotNull(message = "Название должно быть указано")
    val name: String,
    @field:Schema(description = "Адрес ресторана", example = "г. Петровск, ул. Петровская 33")
    @field:NotNull(message = "Адрес должен быть указан")
    val address: String
)