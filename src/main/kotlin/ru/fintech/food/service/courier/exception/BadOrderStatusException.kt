package ru.fintech.food.service.courier.exception

import ru.fintech.food.service.common.exception.BadRequestException

class BadOrderStatusException : BadRequestException("У курьера нет прав на установление данного статуса заказа")