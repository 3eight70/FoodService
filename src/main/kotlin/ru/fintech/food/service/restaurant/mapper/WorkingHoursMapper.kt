package ru.fintech.food.service.restaurant.mapper

import ru.fintech.food.service.restaurant.dto.workinghours.WorkingHoursDto
import ru.fintech.food.service.restaurant.dto.workinghours.WorkingHoursCreateDto
import ru.fintech.food.service.restaurant.entity.Restaurant
import ru.fintech.food.service.restaurant.entity.WorkingHours

object WorkingHoursMapper {
    fun WorkingHours(workingHoursCreateDto: WorkingHoursCreateDto, restaurant: Restaurant) =
        WorkingHours(
            restaurant = restaurant,
            dayOfWeek = workingHoursCreateDto.dayOfWeek,
            openingTime = workingHoursCreateDto.openingTime,
            closingTime = workingHoursCreateDto.closingTime
        )

    fun WorkingHoursDto(workingHours: WorkingHours) =
        WorkingHoursDto(
            id = workingHours.id,
            restaurantId = workingHours.restaurant.id,
            dayOfWeek = workingHours.dayOfWeek,
            openingTime = workingHours.openingTime,
            closingTime = workingHours.closingTime
        )
}