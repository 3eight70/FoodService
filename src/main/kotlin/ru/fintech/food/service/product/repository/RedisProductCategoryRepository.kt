package ru.fintech.food.service.product.repository

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import ru.fintech.food.service.product.dto.category.ProductCategoryDto

@Repository
class RedisProductCategoryRepository(
    private val template: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {
    companion object {
        private const val CATEGORY_KEY_PREFIX = "product:category:"
        private const val ALL_CATEGORIES = "menu:allCategories"
    }

    fun saveCategory(categoryId: String, categoryDto: ProductCategoryDto) {
        val key = getCategoryKey(categoryId)
        val json = objectMapper.writeValueAsString(categoryDto)

        template.opsForValue().set(key, json)

        template.opsForSet().add(ALL_CATEGORIES, categoryId)
    }

    fun getAllCategories(): List<ProductCategoryDto> {
        val categoryIds = template.opsForSet().members(ALL_CATEGORIES) ?: emptySet()

        return categoryIds
            .mapNotNull(this::getCategory)
    }

    fun getCategory(productId: String): ProductCategoryDto? {
        val key = getCategoryKey(productId)
        val json = template.opsForValue().get(key)
        return json
            ?.let { objectMapper.readValue(it, ProductCategoryDto::class.java) }
    }

    fun deleteCategory(categoryId: String) {
        val key = getCategoryKey(categoryId)

        template.delete(key)

        template.opsForSet().remove(ALL_CATEGORIES, categoryId)
    }

    fun checkCategoryByKey(productId: String): Boolean {
        val key = getCategoryKey(productId)
        return template.hasKey(key)
    }

    private fun getCategoryKey(categoryId: String) = CATEGORY_KEY_PREFIX + categoryId
}