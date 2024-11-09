package ru.fintech.food.service.common.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.time.format.DateTimeFormatter

@Schema(description = "dto для ответов на запросы")
class Response(
    @Schema(description = "Код ответа", example = "200")
    val status: Int,
    @Schema(description = "Сообщение ответа", example = "Что-то на успешном")
    val message: String?,
    @Schema(description = "Время ответа", example = "2024-11-09T07:13:57.615242600Z")
    val timestamp: String = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
)