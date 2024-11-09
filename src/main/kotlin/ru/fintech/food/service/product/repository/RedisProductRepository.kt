package ru.fintech.food.service.product.repository

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import ru.fintech.food.service.product.dto.product.FullProductDto

@Repository
class RedisProductRepository(
    private val template: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {
    companion object {
        private const val PRODUCT_KEY_PREFIX = "menu:position:"
        private const val ALL_PRODUCTS = "menu:allProducts"
    }

    fun saveMenuItem(productId: String, product: FullProductDto) {
        val key = getProductKey(productId)
        val json = objectMapper.writeValueAsString(product)

        template.opsForValue().set(key, json)

        template.opsForSet().add(ALL_PRODUCTS, productId)
    }

    fun getAllMenuItems(): List<FullProductDto> {
        val productIds = template.opsForSet().members(ALL_PRODUCTS) ?: emptySet()

        return productIds
            .mapNotNull(this::getMenuItem)
    }

    fun getMenuItem(productId: String): FullProductDto? {
        val key = getProductKey(productId)
        val json = template.opsForValue().get(key)
        return json
            ?.let { objectMapper.readValue(it, FullProductDto::class.java) }
    }

    fun deleteMenuItem(productId: String) {
        val key = getProductKey(productId)

        template.delete(key)

        template.opsForSet().remove(ALL_PRODUCTS, productId)
    }

    fun checkMenuItemByKey(productId: String): Boolean {
        val key = getProductKey(productId)
        return template.hasKey(key)
    }

    private fun getProductKey(productId: String) = PRODUCT_KEY_PREFIX + productId
}