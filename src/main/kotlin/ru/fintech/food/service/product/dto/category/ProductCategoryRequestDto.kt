package ru.fintech.food.service.product.dto.category

import com.fasterxml.jackson.annotation.JsonCreator
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Schema(description = "dto для создания/изменения категорий")
class ProductCategoryRequestDto @JsonCreator constructor(
    @field:Schema(description = "Название категории", example = "Суп")
    @field:NotNull(message = "Название должно быть заполнено")
    @field:NotBlank(message = "Название должно быть заполнено")
    @field:Size(min = 1)
    val name: String
)