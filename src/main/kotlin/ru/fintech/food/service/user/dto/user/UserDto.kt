package ru.fintech.food.service.user.dto.user

import java.util.*

class UserDto (
    val id: UUID,
    val email: String,
    val phoneNumber: String,
    val role: RoleEnum
)