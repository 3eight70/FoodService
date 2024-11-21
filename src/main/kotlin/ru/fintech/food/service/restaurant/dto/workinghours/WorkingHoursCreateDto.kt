package ru.fintech.food.service.restaurant.dto.workinghours

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.time.LocalTime
import java.util.UUID
import ru.fintech.food.service.restaurant.dto.DayOfWeekEnum

@Schema(description = "dto для создания рабочих часов ресторана")
data class WorkingHoursCreateDto(
    @field:Schema(description = "Идентификатор ресторана", example = "3c03e1de-2177-43b2-aecb-aa4e9ff66a22")
    @field:NotNull(message = "Идентификатор ресторана должен быть указан")
    val restaurantId: UUID,
    @field:Schema(description = "День недели", example = "MONDAY")
    @field:NotNull(message = "День недели должен быть указан")
    val dayOfWeek: DayOfWeekEnum,
    @field:Schema(description = "Время открытия")
    @field:NotNull(message = "Время открытия должно быть указано")
    val openingTime: LocalTime,
    @field:Schema(description = "Время закрытия")
    @field:NotNull(message = "Время закрытия должно быть указано")
    val closingTime: LocalTime
)