package ru.fintech.food.service.image.exception

import java.util.UUID
import ru.fintech.food.service.common.exception.BadRequestException

class ImageUsedException(imageId: UUID) :
    BadRequestException("Изображение с id: $imageId используется в меню")