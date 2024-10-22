package ru.fintech.food.service.product.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

/**
 * Сущность категории позиции
 */
@Entity
@Table(name = "t_categories")
class ProductCategory(
    /**
     * Идентификатор категории
     */
    @Id
    val id: UUID = UUID.randomUUID(),
    /**
     * Название категории
     */
    @Column(name = "name", nullable = false)
    val name: String
)