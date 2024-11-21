package ru.fintech.food.service.image.exception

import java.util.UUID
import ru.fintech.food.service.common.exception.NotFoundException

class ImageNotFoundException(id: UUID) : NotFoundException("Изображение с id: $id не найдено")