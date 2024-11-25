package ru.fintech.food.service.bucket.exception

import ru.fintech.food.service.common.exception.BadRequestException
import java.util.UUID

class ProductNotInBucketException(id: UUID) : BadRequestException("Продукт с id: $id не найден в корзине")