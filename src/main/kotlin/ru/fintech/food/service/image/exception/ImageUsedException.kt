package ru.fintech.food.service.image.exception

import ru.fintech.food.service.common.exception.BadRequestException
import java.util.UUID

class ImageUsedException(imageId: UUID) :
    BadRequestException("Изображение с id: $imageId используется в меню")