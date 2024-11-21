package ru.fintech.food.service.product.exception

import java.util.UUID
import ru.fintech.food.service.common.exception.NotFoundException

class ProductCategoryNotFoundException(categoryId: UUID) : NotFoundException("Категория с id: $categoryId не найдена")