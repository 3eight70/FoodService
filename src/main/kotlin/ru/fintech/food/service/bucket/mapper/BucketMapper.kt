package ru.fintech.food.service.bucket.mapper

import ru.fintech.food.service.bucket.dto.BucketDto
import ru.fintech.food.service.bucket.dto.ProductInBucketDto
import ru.fintech.food.service.bucket.entity.Bucket
import ru.fintech.food.service.product.entity.Product
import java.math.BigDecimal

object BucketMapper {
    fun BucketDto(productsInBucket: List<ProductInBucketDto>) =
        BucketDto(
            productsInBucket = productsInBucket,
            price = if (productsInBucket.isEmpty())
                BigDecimal.ZERO
            else
                productsInBucket
                    .map { it.price }
                    .reduce { acc, price -> acc + price }
        )

    fun ProductInBucketDto(bucket: Bucket, product: Product) =
        ProductInBucketDto(
            id = product.id,
            name = product.name,
            amount = bucket.quantity,
            price = product.price.multiply(BigDecimal(bucket.quantity)),
        )

}