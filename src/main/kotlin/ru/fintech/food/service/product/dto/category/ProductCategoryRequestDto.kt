package ru.fintech.food.service.product.dto.category

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Schema(description = "dto для создания/изменения категорий")
class ProductCategoryRequestDto (
    @Schema(description = "Название категории", example = "Суп")
    @NotNull(message = "Название должно быть заполнено")
    @NotBlank(message = "Название должно быть заполнено")
    @Size(min = 1)
    val name: String
)