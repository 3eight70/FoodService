package ru.fintech.food.service.order.exception

import ru.fintech.food.service.common.exception.BadRequestException
import ru.fintech.food.service.restaurant.dto.DayOfWeekEnum

class RestaurantDoesNotWorkInCurrentDayException(dayOfWeekEnum: DayOfWeekEnum) :
    BadRequestException("Ресторан не работает в $dayOfWeekEnum")