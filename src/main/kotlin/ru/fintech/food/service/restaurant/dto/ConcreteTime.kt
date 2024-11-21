package ru.fintech.food.service.restaurant.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(description = "dto для конкретного времени")
data class ConcreteTime(
    @field:Schema(description = "Конкретное время", example = "12:00")
    @field:NotNull(message = "Время должно быть указано")
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty(value = "time")
    val time: String,
)