package ru.fintech.food.service.product.dto.product

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import ru.fintech.food.service.utils.validation.ValidUUID
import java.math.BigDecimal
import java.util.UUID

@Schema(description = "dto для создания/изменения позиции")
class ProductRequestDto (
    @field:Schema(description = "Название позиции", example = "Грибной суп")
    @field:NotNull(message = "Название должно быть заполнено")
    @field:NotBlank(message = "Название должно быть заполнено")
    @field:Size(min = 1)
    val name: String,
    @field:Schema(description = "Описание позиции", example = "Суп, приготовленный из свежайших белых грибов")
    @field:NotNull(message = "Описание должно быть заполнено")
    @field:NotBlank(message = "Описание должно быть заполнено")
    @field:Size(min = 1)
    val description: String,
    @field:Schema(description = "Цена позиции", example = "200")
    @field:NotNull(message = "Название должно быть заполнено")
    val price: BigDecimal,
    @field:Schema(description = "Идентификатор изображения", example = "3c03e1de-2177-43b2-aecb-aa4e9ff66a23")
    @field:NotNull(message = "Идентификатор изображения должен быть указан")
    val imageId: UUID,
    @field:Schema(description = "Находится в наличии", example = "true")
    @field:NotNull(message = "Статус нахождения в наличии должен быть указан")
    val available: Boolean,
    @field:Schema(description = "Идентификаторы категорий", example = "[\"ca1faba3-6894-4e96-93db-5e1ec75f7327\"]")
    @field:NotNull(message = "Категории должны быть указаны")
    @field:Size(min = 1, message = "Множество должно хранить хотя бы 1 элемент")
    @field:ValidUUID
    val categoryIds: Set<String>
)