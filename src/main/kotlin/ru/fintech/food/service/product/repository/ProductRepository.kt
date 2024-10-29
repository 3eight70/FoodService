package ru.fintech.food.service.product.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.fintech.food.service.product.entity.Product
import java.util.*

@Repository
interface ProductRepository : JpaRepository<Product, UUID> {
}