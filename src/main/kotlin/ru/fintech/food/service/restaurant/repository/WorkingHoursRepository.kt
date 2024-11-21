package ru.fintech.food.service.restaurant.repository

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.fintech.food.service.restaurant.dto.DayOfWeekEnum
import ru.fintech.food.service.restaurant.entity.Restaurant
import ru.fintech.food.service.restaurant.entity.WorkingHours

@Repository
interface WorkingHoursRepository : JpaRepository<WorkingHours, UUID> {
    fun findWorkingHoursByRestaurantIdAndDayOfWeek(
        restaurantId: UUID,
        dayOfWeekEnum: DayOfWeekEnum
    ): WorkingHours?

    fun findWorkingHoursByRestaurantId(
        restaurantId: UUID
    ): List<WorkingHours>

    fun findByDayOfWeek(dayOfWeekEnum: DayOfWeekEnum): List<WorkingHours>

    fun deleteAllByRestaurant(restaurant: Restaurant)
}