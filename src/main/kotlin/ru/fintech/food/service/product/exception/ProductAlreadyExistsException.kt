package ru.fintech.food.service.product.exception

import ru.fintech.food.service.common.exception.BadRequestException

class ProductAlreadyExistsException(
    name: String
) : BadRequestException("Продукт с названием: $name уже существует")