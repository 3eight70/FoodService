package ru.fintech.food.service.common.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.time.format.DateTimeFormatter


@Schema(description = "Ответ на запросы")
class ErrorResponse(
    @field:Schema(description = "Статус ответа", example = "200")
    @field:JsonProperty("status")
    private val status: Int,

    @field:Schema(description = "Сообщение ответа", example = "Категория успешно удалена")
    @field:JsonProperty("message")
    private val message: String,

    @field:Schema(description = "Время ответа", example = "2024-11-09T07:13:57.615242600Z")
    @field:JsonProperty("timestamp")
    private val timestamp: String = DateTimeFormatter.ISO_INSTANT.format(Instant.now()),

    @ArraySchema(schema = Schema(implementation = ErrorDto::class))
    @field:JsonProperty("errors")
    private val errors: MutableList<ErrorDto> = mutableListOf()
)