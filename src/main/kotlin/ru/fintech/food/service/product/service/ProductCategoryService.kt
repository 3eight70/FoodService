package ru.fintech.food.service.product.service

import ru.fintech.food.service.common.dto.Response
import ru.fintech.food.service.product.dto.category.ProductCategoryDto
import ru.fintech.food.service.product.dto.category.ProductCategoryRequestDto
import ru.fintech.food.service.user.dto.user.UserDto
import java.util.*

interface ProductCategoryService {
    fun getCategories(): Set<ProductCategoryDto>
    fun getCategoryById(categoryId: UUID): ProductCategoryDto
    fun createCategory(userDto: UserDto, categoryRequestDto: ProductCategoryRequestDto): ProductCategoryDto
    fun updateCategory(userDto: UserDto, categoryRequestDto: ProductCategoryRequestDto): ProductCategoryDto
    fun deleteCategory(userDto: UserDto, categoryId: UUID): Response
}