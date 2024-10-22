package ru.fintech.food.service.product.exception

import ru.fintech.food.service.common.exception.BadRequestException

class ProductCategoryAlreadyExistsException(name: String) :
    BadRequestException("Категория с названием: $name уже существует")
