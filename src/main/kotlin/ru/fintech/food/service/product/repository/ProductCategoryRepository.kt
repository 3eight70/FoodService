package ru.fintech.food.service.product.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.fintech.food.service.product.entity.ProductCategory
import java.util.Optional
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Repository
interface ProductCategoryRepository : JpaRepository<ProductCategory, UUID> {
    fun findByName(name: String): CompletableFuture<ProductCategory>
    fun findProductCategoryById(id: UUID): CompletableFuture<ProductCategory>
}