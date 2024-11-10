package ru.fintech.food.service.image.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.fintech.food.service.image.entity.Image
import java.util.UUID

interface ImageRepository : JpaRepository<Image, UUID> {
    fun findImageByNameContainsIgnoreCase(name: String): Image?
}