package ru.fintech.food.service.restaurant.dto.workinghours

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalTime
import java.util.UUID
import ru.fintech.food.service.restaurant.dto.DayOfWeekEnum

@Schema(description = "dto для рабочих часов ресторана")
data class WorkingHoursDto(
    @field:Schema(description = "Идентификатор выходного дня", example = "3c03e1de-2177-43b2-aecb-aa4e9ff66a23")
    val id: UUID,
    @field:Schema(description = "Идентификатор ресторана", example = "3c03e1de-2177-43b2-aecb-aa4e9ff66a22")
    val restaurantId: UUID,
    @field:Schema(description = "День недели", example = "MONDAY")
    val dayOfWeek: DayOfWeekEnum,
    @field:Schema(description = "Время открытия")
    val openingTime: LocalTime,
    @field:Schema(description = "Время закрытия")
    val closingTime: LocalTime
)