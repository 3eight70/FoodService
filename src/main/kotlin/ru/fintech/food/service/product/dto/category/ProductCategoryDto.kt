package ru.fintech.food.service.product.dto.category

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(description =  "dto для категорий продуктов")
class ProductCategoryDto(
    @Schema(description = "Идентификатор категории", example = "3c03e1de-2177-43b2-aecb-aa4e9ff66a23")
    val id: UUID,
    @Schema(description = "Название категории", example = "Суп")
    val name: String
)