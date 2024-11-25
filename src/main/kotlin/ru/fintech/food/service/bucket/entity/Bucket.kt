package ru.fintech.food.service.bucket.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "t_bucket")
class Bucket(
    @Id
    val id: UUID = UUID.randomUUID(),
    @Column(name = "user_id", nullable = false)
    val userId: UUID,
    @Column(name = "product_id", nullable = false)
    val productId: UUID,
    @Column(name = "quantity", nullable = false)
    var quantity: Int
)