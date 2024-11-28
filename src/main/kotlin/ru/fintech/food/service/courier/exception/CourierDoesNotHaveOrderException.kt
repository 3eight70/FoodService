package ru.fintech.food.service.courier.exception

import java.util.UUID
import ru.fintech.food.service.common.exception.NotFoundException

class CourierDoesNotHaveOrderException(orderId: UUID) : NotFoundException("Заказ с id: $orderId не найден у курьера")