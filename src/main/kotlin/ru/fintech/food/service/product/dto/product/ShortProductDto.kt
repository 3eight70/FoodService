package ru.fintech.food.service.product.dto.product

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.util.UUID

@Schema(description = "dto для позиций меню")
class ShortProductDto (
    @Schema(description = "Идентификатор продукта", example = "3c03e1de-2177-43b2-aecb-aa4e9ff66a23")
    val id: UUID,
    @Schema(description = "Название позиции", example = "Грибной суп")
    val name: String,
    @Schema(description = "Описание позиции", example = "Суп, приготовленный из свежайших белых грибов")
    val description: String,
    @Schema(description = "Цена позиции", example = "200")
    val price: BigDecimal,
    @Schema(description = "Идентификатор изображения", example = "3c03e1de-2177-43b2-aecb-aa4e9ff66a23")
    val imageId: UUID,
    @Schema(description = "Находится в наличии", example = "true")
    val isAvailable: Boolean
)