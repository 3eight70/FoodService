package ru.fintech.food.service.product.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.UUID

/**
 * Сущность для позиции меню
 */
@Entity
@Table(name = "t_products")
class Product(
    /**
     * Идентификатор позиции
     */
    @Id
    val id: UUID = UUID.randomUUID(),
    /**
     * Название позиции
     */
    @Column(name = "name", nullable = false, unique = true)
    var name: String,
    /**
     * Описание позиции меню
     */
    @Column(name = "description", nullable = false)
    var description: String,
    /**
     * Цена позиции
     */
    @Column(name = "price", nullable = false)
    var price: BigDecimal,
    /**
     * Идентификатор изображения
     */
    @Column(name = "image_id", nullable = false)
    var imageId: UUID,
    /**
     * Есть в наличии
     */
    @Column(name = "available", nullable = false)
    var available: Boolean = false,
    /**
     * Категории, к которым относится позиция
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "t_product_categories",
        joinColumns = [JoinColumn(name = "product_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")]
    )
    var categories: Set<ProductCategory> = HashSet()
)