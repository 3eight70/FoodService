package ru.fintech.food.service.product.exception

import ru.fintech.food.service.common.exception.NotFoundException
import java.util.UUID

class ProductCategoryNotFoundException(categoryId: UUID) : NotFoundException("Категория с id: $categoryId не найдена")