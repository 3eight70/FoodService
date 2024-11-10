package ru.fintech.food.service.user.mapper

import ru.fintech.food.service.user.dto.user.UserDto
import ru.fintech.food.service.user.entity.User

object UserMapper {
    fun UserDto(user: User): UserDto =
        UserDto(
            id = user.id,
            email = user.email,
            phoneNumber = user.phoneNumber,
            role = user.role,
            isConfirmed = user.isConfirmed
        )
}