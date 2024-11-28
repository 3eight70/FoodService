package ru.fintech.food.service.courier.exception

import ru.fintech.food.service.common.exception.BadRequestException

class OrderIsNotReadyForDeliveryException : BadRequestException("Заказ еще недоступен для доставки")