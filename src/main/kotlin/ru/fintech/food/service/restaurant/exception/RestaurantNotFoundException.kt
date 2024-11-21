package ru.fintech.food.service.restaurant.exception

import java.util.UUID
import ru.fintech.food.service.common.exception.NotFoundException

class RestaurantNotFoundException(val id: UUID) : NotFoundException("Ресторан с id: $id не найден")