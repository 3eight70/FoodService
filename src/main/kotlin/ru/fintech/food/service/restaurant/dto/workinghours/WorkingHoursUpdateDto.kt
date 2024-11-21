package ru.fintech.food.service.restaurant.dto.workinghours

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.time.LocalTime

@Schema(description = "dto для обновления рабочих часов ресторана")
data class WorkingHoursUpdateDto(
    @field:Schema(description = "Время открытия")
    @field:NotNull(message = "Время открытия должно быть указано")
    val openingTime: LocalTime,
    @field:Schema(description = "Время закрытия")
    @field:NotNull(message = "Время закрытия должно быть указано")
    val closingTime: LocalTime
)