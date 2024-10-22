package ru.fintech.food.service.product.service

import ru.fintech.food.service.product.dto.product.FullProductDto
import ru.fintech.food.service.product.dto.product.ShortProductDto
import java.util.UUID

interface ProductService {
    fun getProducts(): List<ShortProductDto>
    fun getInfoAboutProduct(productId: UUID): FullProductDto
    fun getProductsByCategory(categoryId: UUID): List<ShortProductDto>
    fun getUnavailableProducts(): List<ShortProductDto>
}