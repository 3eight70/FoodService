package ru.fintech.food.service.product.exception

import ru.fintech.food.service.common.exception.NotFoundException
import java.util.UUID

class ProductNotFoundException(
    productId: UUID,
) : NotFoundException("Продукт с id: $productId не найден")