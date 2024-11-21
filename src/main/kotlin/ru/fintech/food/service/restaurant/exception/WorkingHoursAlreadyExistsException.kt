package ru.fintech.food.service.restaurant.exception

import java.util.UUID
import ru.fintech.food.service.common.exception.BadRequestException
import ru.fintech.food.service.restaurant.dto.DayOfWeekEnum

class WorkingHoursAlreadyExistsException(
    restaurantId: UUID, dayOfWeekEnum: DayOfWeekEnum
) : BadRequestException("Для ресторана с id: $restaurantId уже назначены рабочие часы на $dayOfWeekEnum")