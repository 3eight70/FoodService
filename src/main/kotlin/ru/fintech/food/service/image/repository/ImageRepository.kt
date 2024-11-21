package ru.fintech.food.service.image.repository

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import ru.fintech.food.service.image.entity.Image

interface ImageRepository : JpaRepository<Image, UUID> {
    fun findImageByNameContainsIgnoreCase(name: String): Image?
}