package ru.fintech.food.service.order.exception

import java.util.UUID
import ru.fintech.food.service.common.exception.NotFoundException

class OrderNotFoundException(orderId: UUID) : NotFoundException("Заказ с id: $orderId не найден")