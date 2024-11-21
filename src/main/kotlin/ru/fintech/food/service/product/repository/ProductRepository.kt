package ru.fintech.food.service.product.repository

import java.util.UUID
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.fintech.food.service.product.entity.Product

@Repository
interface ProductRepository : JpaRepository<Product, UUID> {
    fun findByAvailableIsFalse(): List<Product>
    fun findProductByName(name: String): Product?
    fun existsByImageId(imageId: UUID): Boolean

    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.id IN :categoryIdList")
    fun findByCategoryIdIn(@Param("categoryIdList") categoryIdList: List<UUID>, pageable: Pageable): Page<Product>
}