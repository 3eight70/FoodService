package ru.fintech.food.service.product.mapper

import ru.fintech.food.service.product.dto.product.FullProductDto
import ru.fintech.food.service.product.dto.product.ProductRequestDto
import ru.fintech.food.service.product.dto.product.ShortProductDto
import ru.fintech.food.service.product.entity.Product

object ProductMapper {
    fun ShortProductDto(product: Product): ShortProductDto =
        ShortProductDto(
            id = product.id,
            name = product.name,
            description = product.description,
            price = product.price,
            imageId = product.imageId,
            available = product.available
        )

    fun FullProductDto(product: Product): FullProductDto =
        FullProductDto(
            id = product.id,
            name = product.name,
            description = product.description,
            price = product.price,
            imageId = product.imageId,
            available = product.available,
            categories = product.categories
                .map(ProductCategoryMapper::ProductCategoryDto)
                .toSet()
        )

    fun Product(productRequestDto: ProductRequestDto): Product =
        Product(
            name = productRequestDto.name,
            description = productRequestDto.description,
            price = productRequestDto.price,
            imageId = productRequestDto.imageId,
            available = productRequestDto.available
        )

    fun ShortProductDto(fullProductDto: FullProductDto): ShortProductDto =
        ShortProductDto(
            id = fullProductDto.id,
            name = fullProductDto.name,
            description = fullProductDto.description,
            price = fullProductDto.price,
            imageId = fullProductDto.imageId,
            available = fullProductDto.available
        )
}