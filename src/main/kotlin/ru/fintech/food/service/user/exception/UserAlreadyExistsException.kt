package ru.fintech.food.service.user.exception

import ru.fintech.food.service.common.exception.BadRequestException

class UserAlreadyExistsException(message: String) : BadRequestException(message)