package ru.fintech.food.service.order.exception

import ru.fintech.food.service.common.exception.BadRequestException

class BadDeliveryTimeException(message: String) : BadRequestException(message)