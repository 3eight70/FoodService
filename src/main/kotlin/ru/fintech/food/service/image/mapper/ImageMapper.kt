package ru.fintech.food.service.image.mapper

import org.springframework.web.multipart.MultipartFile
import ru.fintech.food.service.image.dto.ImageDto
import ru.fintech.food.service.image.entity.Image

object ImageMapper {
    fun Image(multipartFile: MultipartFile, authorEmail: String) =
        Image(
            name = multipartFile.originalFilename!!,
            size = multipartFile.size,
            authorEmail = authorEmail
        )

    fun ImageDto(image: Image) =
        ImageDto(
            id = image.id,
            size = image.size,
            authorEmail = image.authorEmail,
            uploadTimestamp = image.uploadTimestamp,
            imageName = image.name,
        )
}