package ru.fintech.food.service.product.dto.product

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.util.*
import ru.fintech.food.service.utils.validation.ValidUUID

@Schema(description = "dto для создания/изменения позиции")
class ProductRequestDto (
    @Schema(description = "Название позиции", example = "Грибной суп")
    @field:NotNull(message = "Название должно быть заполнено")
    @field:NotBlank(message = "Название должно быть заполнено")
    @field:Size(min = 1)
    val name: String,
    @Schema(description = "Описание позиции", example = "Суп, приготовленный из свежайших белых грибов")
    @field:NotNull(message = "Описание должно быть заполнено")
    @field:NotBlank(message = "Описание должно быть заполнено")
    @field:Size(min = 1)
    val description: String,
    @Schema(description = "Цена позиции", example = "200")
    @field:NotNull(message = "Название должно быть заполнено")
    val price: BigDecimal,
    @Schema(description = "Идентификатор изображения", example = "3c03e1de-2177-43b2-aecb-aa4e9ff66a23")
    @field:NotNull(message = "Идентификатор изображения должен быть указан")
    val imageId: UUID,
    @Schema(description = "Находится в наличии", example = "true")
    @field:NotNull(message = "Статус нахождения в наличии должен быть указан")
    val available: Boolean,
    @Schema(description = "Идентификаторы категорий")
    @field:NotNull(message = "Категории должны быть указаны")
    @field:Size(min = 1, message = "Множество должно хранить хотя бы 1 элемент")
    @field:ValidUUID
    val categoryIds: Set<String>
)