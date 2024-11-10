package ru.fintech.food.service.image.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

/**
 * Сущность для изображений
 */
@Entity
@Table(name = "t_images")
class Image(
    @Id
    val id: UUID = UUID.randomUUID(),
    @Column(name = "upload_time", nullable = false)
    val uploadTimestamp: Instant = Instant.now(),
    @Column(name = "image_name", nullable = false)
    val name : String,
    @Column(name = "size", nullable = false)
    val size: Long,
    @Column(name = "author_email", nullable = false)
    val authorEmail: String
)