package ru.fintech.food.service.restaurant.mapper

import ru.fintech.food.service.restaurant.dto.restaurant.RestaurantDto
import ru.fintech.food.service.restaurant.dto.restaurant.RestaurantRequestDto
import ru.fintech.food.service.restaurant.entity.Restaurant

object RestaurantMapper {
    fun Restaurant(restaurantRequestDto: RestaurantRequestDto) =
        Restaurant(
            name = restaurantRequestDto.name,
            address = restaurantRequestDto.address
        )

    fun RestaurantDto(restaurant: Restaurant) =
        RestaurantDto(
            id = restaurant.id,
            name = restaurant.name,
            address = restaurant.address
        )
}