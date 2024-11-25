package ru.fintech.food.service.bucket.dto

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@Schema(description = "dto для корзины")
class BucketDto(
    @field:ArraySchema(
        arraySchema = Schema(
            description = "Список продуктов в корзине",
            implementation = ProductInBucketDto::class
        )
    )
    val productsInBucket: List<ProductInBucketDto>,
    @field:Schema(description = "Общая цена продуктов в корзине", example = "533")
    val price: BigDecimal
)