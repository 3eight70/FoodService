package ru.fintech.food.service.user.exception

import java.util.UUID
import ru.fintech.food.service.common.exception.NotFoundException

class UserNotFoundException(id: UUID) : NotFoundException("Пользователь с id: $id не найден")