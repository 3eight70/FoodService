package ru.fintech.food.service.image.exception

import ru.fintech.food.service.common.exception.NotFoundException
import java.util.UUID

class ImageNotFoundException(id: UUID) : NotFoundException("Изображение с id: $id не найдено")