package ru.fintech.food.service.bucket.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.util.UUID

@Schema(description = "dto для продуктов в корзине")
class ProductInBucketDto (
    @field:Schema(description = "Идентификатор продукта", example = "3c03e1de-2177-43b2-aecb-aa4e9ff66a23")
    val id: UUID,
    @field:Schema(description = "Название продукта", example = "Бутерброд")
    val name: String,
    @field:Schema(description = "Количество продуктов в корзине", example = "3")
    val amount: Int,
    @field:Schema(description = "Цена продуктов в меню", example = "300")
    val price: BigDecimal
)