package ru.fintech.food.service.order.exception

import java.time.LocalTime
import ru.fintech.food.service.common.exception.BadRequestException

class RestaurantDoesNotWorkInChosenTimeException(time: LocalTime) :
    BadRequestException("Ресторан не работает в $time")