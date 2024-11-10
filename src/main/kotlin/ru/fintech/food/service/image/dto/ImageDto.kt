package ru.fintech.food.service.image.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.util.UUID

@Schema(description = "dto для изображений")
data class ImageDto(
    @field:Schema(description = "Идентификатор изображения")
    val id: UUID,
    @field:Schema(description = "Время загрузки изображения")
    val uploadTimestamp: Instant,
    @field:Schema(description = "Название изображения", example = "Картинка")
    val imageName: String,
    @field:Schema(description = "Размер изображения")
    val size: Long,
    @field:Schema(description = "Электронная почта пользователя, загрузившего изображение")
    val authorEmail: String
)