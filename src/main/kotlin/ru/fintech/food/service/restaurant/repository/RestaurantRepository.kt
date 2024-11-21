package ru.fintech.food.service.restaurant.repository

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.fintech.food.service.restaurant.entity.Restaurant

@Repository
interface RestaurantRepository : JpaRepository<Restaurant, UUID> {
}