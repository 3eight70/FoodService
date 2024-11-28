package ru.fintech.food.service.admin.exception

import ru.fintech.food.service.common.exception.BadRequestException

class UserIsNotCourierException : BadRequestException("Пользователь не является курьером")