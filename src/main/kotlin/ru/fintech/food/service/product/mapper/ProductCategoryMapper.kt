package ru.fintech.food.service.product.mapper

import ru.fintech.food.service.product.dto.category.ProductCategoryDto
import ru.fintech.food.service.product.dto.category.ProductCategoryRequestDto
import ru.fintech.food.service.product.entity.ProductCategory

object ProductCategoryMapper {
    fun ProductCategoryDto(category: ProductCategory) =
        ProductCategoryDto(
            id = category.id,
            name = category.name
        )

    fun ProductCategory(categoryRequestDto: ProductCategoryRequestDto) =
        ProductCategory(
            name = categoryRequestDto.name
        )
}