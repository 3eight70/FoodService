package ru.fintech.food.service.bucket.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.fintech.food.service.bucket.entity.Bucket
import java.util.UUID

interface BucketRepository : JpaRepository<Bucket, UUID> {
    fun findAllByUserId(userId: UUID): List<Bucket>
    fun findByUserIdAndProductId(userId: UUID, productId: UUID): Bucket?
    fun deleteAllByUserId(userId: UUID)
}