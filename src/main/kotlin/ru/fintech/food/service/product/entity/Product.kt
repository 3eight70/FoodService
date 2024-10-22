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
import java.util.*

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
    @Column(name = "name", nullable = false)
    val name: String,
    /**
     * Описание позиции меню
     */
    @Column(name = "description", nullable = false)
    val description: String,
    /**
     * Цена позиции
     */
    @Column(name = "price", nullable = false)
    val price: BigDecimal,
    /**
     * Идентификатор изображения
     */
    //TODO: Реализовать S3 хранилище - minio и отдельную бд для хранения id изображения в minio
    @Column(name = "image_id", nullable = false)
    val imageId: UUID,
    /**
     * Есть в наличии
     */
    val isAvailable: Boolean,
    /**
     * Категории, к которым относится позиция
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "t_product_categories",
        joinColumns = [JoinColumn(name = "product_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")]
    )
    val categories: Set<ProductCategory> = HashSet()
)