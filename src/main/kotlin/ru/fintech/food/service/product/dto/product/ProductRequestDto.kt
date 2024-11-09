package ru.fintech.food.service.product.dto.product

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.util.*

@Schema(description = "dto для создания/изменения позиции")
class ProductRequestDto (
    @Schema(description = "Название позиции", example = "Грибной суп")
    @NotNull(message = "Название должно быть заполнено")
    @NotBlank(message = "Название должно быть заполнено")
    @Size(min = 1)
    val name: String,
    @Schema(description = "Описание позиции", example = "Суп, приготовленный из свежайших белых грибов")
    @NotNull(message = "Описание должно быть заполнено")
    @NotBlank(message = "Описание должно быть заполнено")
    @Size(min = 1)
    val description: String,
    @Schema(description = "Цена позиции", example = "200")
    @NotNull(message = "Название должно быть заполнено")
    val price: BigDecimal,
    @Schema(description = "Идентификатор изображения", example = "3c03e1de-2177-43b2-aecb-aa4e9ff66a23")
    @NotNull(message = "Идентификатор изображения должен быть указан")
    val imageId: UUID,
    @Schema(description = "Находится в наличии", example = "true")
    @NotNull(message = "Статус нахождения в наличии должен быть указан")
    val available: Boolean,
    @Schema(description = "Идентификаторы категорий")
    @NotNull(message = "Категории должны быть указаны")
    @Size(min = 1, message = "Множество должно хранить хотя бы 1 элемент")
    val categoryIds: Set<UUID>
)