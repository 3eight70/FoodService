package ru.fintech.food.service.restaurant.dto.restaurant

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

@Schema(description = "dto для ресторанов доставки")
data class RestaurantDto(
    @field:Schema(description = "Идентификатор ресторана", example = "3c03e1de-2177-43b2-aecb-aa4e9ff66a23")
    val id: UUID,
    @field:Schema(description = "Название ресторана", example = "Пельменная")
    val name: String,
    @field:Schema(description = "Адрес ресторана", example = "г. Петровск, ул. Петровская 33")
    val address: String
)