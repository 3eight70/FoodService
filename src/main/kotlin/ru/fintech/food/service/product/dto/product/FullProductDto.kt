package ru.fintech.food.service.product.dto.product

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.util.UUID
import ru.fintech.food.service.product.dto.category.ProductCategoryDto

@Schema(description = "dto для позиций меню")
class FullProductDto(
    @field:Schema(description = "Идентификатор продукта", example = "3c03e1de-2177-43b2-aecb-aa4e9ff66a23")
    val id: UUID,
    @field:Schema(description = "Название позиции", example = "Грибной суп")
    val name: String,
    @field:Schema(description = "Описание позиции", example = "Суп, приготовленный из свежайших белых грибов")
    val description: String,
    @field:Schema(description = "Цена позиции", example = "200")
    val price: BigDecimal,
    @field:Schema(description = "Идентификатор изображения", example = "3c03e1de-2177-43b2-aecb-aa4e9ff66a23")
    val imageId: UUID,
    @field:Schema(description = "Находится в наличии", example = "true")
    val available: Boolean,
    @field:ArraySchema(
        arraySchema = Schema(
            description = "Список категорий продуктов",
            implementation = ProductCategoryDto::class
        )
    )
    val categories: Set<ProductCategoryDto>
)