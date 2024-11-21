package ru.fintech.food.service.product.repository

import java.util.Optional
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.fintech.food.service.product.entity.ProductCategory

@Repository
interface ProductCategoryRepository : JpaRepository<ProductCategory, UUID> {
    fun findByName(name: String): Optional<ProductCategory>
}