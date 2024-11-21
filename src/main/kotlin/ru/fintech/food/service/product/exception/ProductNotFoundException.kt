package ru.fintech.food.service.product.exception

import java.util.UUID
import ru.fintech.food.service.common.exception.NotFoundException

class ProductNotFoundException(
    productId: UUID,
) : NotFoundException("Продукт с id: $productId не найден")