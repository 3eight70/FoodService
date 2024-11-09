package ru.fintech.food.service.product.specification

import org.springframework.data.jpa.domain.Specification
import ru.fintech.food.service.product.entity.Product
import java.util.UUID

object ProductSpecification {
    fun isInCategory(productCategories: List<UUID>) =
        Specification<Product> { root, _, builder ->
            builder.and(root.get<Product>("categories").`in`(productCategories))
        }
}