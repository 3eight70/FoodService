package ru.fintech.food.service.bucket.repository

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.fintech.food.service.bucket.entity.Bucket

@Repository
interface BucketRepository : JpaRepository<Bucket, UUID> {
    fun findAllByUserId(userId: UUID): List<Bucket>
    fun findByUserIdAndProductId(userId: UUID, productId: UUID): Bucket?
    fun deleteAllByUserId(userId: UUID)
}